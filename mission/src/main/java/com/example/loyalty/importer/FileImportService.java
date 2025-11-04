package com.example.loyalty.importer;

import com.example.loyalty.entity.Product;
import com.example.loyalty.multitenant.TenantContext;
import com.example.loyalty.product.service.ProductService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;

@Service
public class FileImportService {
    private final Logger log = LoggerFactory.getLogger(FileImportService.class);
    private final ImportProperties props;
    private final ProductService productService;

    public FileImportService(ImportProperties props, ProductService productService) {
        this.props = props;
        this.productService = productService;
    }

    @Scheduled(fixedDelayString = "${importer.poll-interval-ms:60000}")
    public void poll() {
        if (props.importerPath == null || props.importerPath.isEmpty()) {
            log.debug("Importer path not configured, skipping poll");
            return;
        }
        try {
            File dir = new File(props.importerPath);
            if (!dir.exists() || !dir.isDirectory()) {
                log.warn("Importer path is not a directory: {}", props.importerPath);
                return;
            }
            File[] files = dir.listFiles(
                    (d, name) -> name.toLowerCase().endsWith(".csv") || name.toLowerCase().endsWith(".xlsx"));
            if (files == null)
                return;
            for (File f : files) {
                try {
                    if (!isStable(f.toPath(), props.stableCheckMs)) {
                        log.debug("File {} is still growing, skipping for now", f.getName());
                        continue;
                    }
                    processFile(f);
                } catch (Exception e) {
                    log.error("Error processing file {}: {}", f.getName(), e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            log.error("Error during polling: {}", e.getMessage(), e);
        }
    }

    private boolean isStable(Path p, long stableCheckMs) throws IOException, InterruptedException {
        long s1 = Files.size(p);
        Thread.sleep(Math.max(200, stableCheckMs));
        long s2 = Files.size(p);
        return s1 == s2;
    }

    private void processFile(File f) throws Exception {
        log.info("Processing file {}", f.getAbsolutePath());
        String name = f.getName().toLowerCase();
        if (name.endsWith(".csv")) {
            processCsv(f);
        } else if (name.endsWith(".xlsx")) {
            processXlsx(f);
        }
        moveToProcessed(f);
    }

    private void moveToProcessed(File f) {
        try {
            Path dir = f.toPath().getParent();
            Path processed = dir.resolve(props.processedSubdir);
            if (!Files.exists(processed))
                Files.createDirectories(processed);
            Path target = processed.resolve(f.getName());
            Files.move(f.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            log.info("Moved {} to processed folder", f.getName());
        } catch (Exception e) {
            log.error("Failed to move processed file {}: {}", f.getName(), e.getMessage());
        }
    }

    private void processCsv(File f) throws Exception {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
            String header = br.readLine();
            if (header == null)
                return;
            String[] cols = header.split(",");
            String line;
            while ((line = br.readLine()) != null) {
                String[] vals = line.split(",");
                Product p = mapRowToProduct(cols, vals);
                saveProduct(p);
            }
        }
    }

    private void processXlsx(File f) throws Exception {
        try (FileInputStream fis = new FileInputStream(f); Workbook wb = WorkbookFactory.create(fis)) {
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            if (!it.hasNext())
                return;
            Row header = it.next();
            int cols = header.getLastCellNum();
            String[] colNames = new String[cols];
            for (int i = 0; i < cols; i++) {
                if (header.getCell(i) != null)
                    colNames[i] = header.getCell(i).getStringCellValue();
            }
            while (it.hasNext()) {
                Row r = it.next();
                String[] vals = new String[cols];
                for (int i = 0; i < cols; i++) {
                    if (r.getCell(i) != null)
                        vals[i] = r.getCell(i).toString();
                }
                Product p = mapRowToProduct(colNames, vals);
                saveProduct(p);
            }
        }
    }

    private Product mapRowToProduct(String[] cols, String[] vals) {
        Product p = new Product();
        for (int i = 0; i < Math.min(cols.length, vals.length); i++) {
            String col = cols[i] == null ? "" : cols[i].trim().toLowerCase();
            String val = vals[i] == null ? "" : vals[i].trim();
            switch (col) {
                case "name":
                    p.name = val;
                    break;
                case "price":
                    try {
                        p.price = Double.parseDouble(val);
                    } catch (Exception e) {
                        p.price = 0;
                    }
                    break;
                case "stock":
                    try {
                        p.stock = (int) Double.parseDouble(val);
                    } catch (Exception e) {
                        p.stock = 0;
                    }
                    break;
                case "description":
                case "desc":
                    // product entity does not have description field; ignore or extend entity
                    break;
                case "tenantid":
                case "tenant_id":
                    // handled in saveProduct
                    p.tenantId = val;
                    break;
                default:
                    // ignore
            }
        }
        return p;
    }

    private void saveProduct(Product p) {
        String originalTenant = TenantContext.getTenantId();
        try {
            if (p.tenantId != null && !p.tenantId.isEmpty()) {
                TenantContext.setTenantId(p.tenantId);
            }
            productService.createProduct(p);
        } catch (Exception e) {
            log.error("Failed saving product {}: {}", p.name, e.getMessage());
        } finally {
            if (originalTenant != null)
                TenantContext.setTenantId(originalTenant);
            else
                TenantContext.clear();
        }
    }
}
