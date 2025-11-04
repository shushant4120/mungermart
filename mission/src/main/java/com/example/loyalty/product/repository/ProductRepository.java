package com.example.loyalty.product.repository;

import com.example.loyalty.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByTenantId(String tenantId);

    Page<Product> findByTenantId(String tenantId, Pageable pageable);

    Page<Product> findByTenantIdAndNameContainingIgnoreCase(String tenantId, String name, Pageable pageable);

    java.util.Optional<Product> findByTenantIdAndId(String tenantId, String id);
}
