package com.example.loyalty.product.repository;

import com.example.loyalty.entity.Product;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ProductRepositoryIT {

    @Autowired
    private ProductRepository productRepo;

    @Value("${spring.data.mongodb.uri:}")
    private String mongoUri;

    @BeforeEach
    void setup() {
        // Skip integration test if no mongo URI provided
        Assumptions.assumeTrue(mongoUri != null && !mongoUri.trim().isEmpty(),
                "MONGO URI not provided. Skipping integration test.");
        productRepo.deleteAll();
    }

    @Test
    void saveAndFindByTenant() {
        Product p = new Product();
        p.name = "IntegrationTomato";
        p.price = 12.5;
        p.stock = 100;
        p.tenantId = "INT-T1";

        productRepo.save(p);

        List<Product> list = productRepo.findByTenantId("INT-T1");
        assertFalse(list.isEmpty());
        assertEquals("IntegrationTomato", list.get(0).name);
    }
}
