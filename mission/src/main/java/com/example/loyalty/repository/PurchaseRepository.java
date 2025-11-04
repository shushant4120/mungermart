package com.example.loyalty.repository;

import com.example.loyalty.entity.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface PurchaseRepository extends MongoRepository<Purchase, String> {
    List<Purchase> findByTenantId(String tenantId);
}