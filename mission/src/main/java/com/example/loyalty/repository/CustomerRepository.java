package com.example.loyalty.repository;

import com.example.loyalty.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface CustomerRepository extends MongoRepository<Customer, String> {
    List<Customer> findByTenantId(String tenantId);

    Optional<Customer> findByTenantIdAndCardId(String tenantId, String cardId);
}