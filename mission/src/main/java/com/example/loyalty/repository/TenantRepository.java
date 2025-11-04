package com.example.loyalty.repository;

import com.example.loyalty.entity.Tenant;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TenantRepository extends MongoRepository<Tenant, String> {
    Optional<Tenant> findByApiKey(String apiKey);
}