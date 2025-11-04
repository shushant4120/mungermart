package com.example.loyalty.repository;

import com.example.loyalty.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByTenantId(String tenantId);

    Page<Product> findByTenantId(String tenantId, Pageable pageable);

    Page<Product> findByTenantIdAndNameContainingIgnoreCase(String tenantId, String name, Pageable pageable);

    java.util.Optional<Product> findByTenantIdAndId(String tenantId, String id);
}