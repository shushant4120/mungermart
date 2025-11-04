package com.example.loyalty.customer.repository;

import com.example.loyalty.customer.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    List<Customer> findByTenantId(String tenantId);

    Optional<Customer> findByTenantIdAndCardId(String tenantId, String cardId);
}