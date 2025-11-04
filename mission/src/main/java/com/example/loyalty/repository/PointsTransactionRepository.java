package com.example.loyalty.repository;

import com.example.loyalty.entity.PointsTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PointsTransactionRepository extends MongoRepository<PointsTransaction, String> {
    List<PointsTransaction> findByTenantIdAndCustomerId(String tenantId, String customerId);
}
