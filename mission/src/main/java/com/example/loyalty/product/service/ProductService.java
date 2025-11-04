package com.example.loyalty.product.service;

import com.example.loyalty.entity.Product;
import com.example.loyalty.multitenant.TenantContext;
import com.example.loyalty.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepo;

    public ProductService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    public Page<Product> listProducts(int page, int size, String q, String sortBy, String direction) {
        String tenantId = TenantContext.getTenantId();
        Sort sort = Sort.by(Sort.Direction.fromString(direction == null ? "ASC" : direction),
                sortBy == null ? "name" : sortBy);
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), sort);
        if (q == null || q.isEmpty()) {
            return productRepo.findByTenantId(tenantId, pageable);
        }
        return productRepo.findByTenantIdAndNameContainingIgnoreCase(tenantId, q, pageable);
    }

    public Optional<Product> getProduct(String id) {
        String tenantId = TenantContext.getTenantId();
        return productRepo.findByTenantIdAndId(tenantId, id);
    }

    public Product createProduct(Product p) {
        String tenantId = TenantContext.getTenantId();
        p.tenantId = tenantId;
        return productRepo.save(p);
    }

    public Product updateProduct(String id, Product p) {
        String tenantId = TenantContext.getTenantId();
        Optional<Product> existing = productRepo.findByTenantIdAndId(tenantId, id);
        if (!existing.isPresent())
            throw new RuntimeException("Product not found");
        Product prod = existing.get();
        prod.name = p.name != null ? p.name : prod.name;
        prod.price = p.price;
        prod.stock = p.stock;
        return productRepo.save(prod);
    }

    public void deleteProduct(String id) {
        String tenantId = TenantContext.getTenantId();
        Optional<Product> existing = productRepo.findByTenantIdAndId(tenantId, id);
        if (!existing.isPresent())
            throw new RuntimeException("Product not found");
        productRepo.delete(existing.get());
    }
}
