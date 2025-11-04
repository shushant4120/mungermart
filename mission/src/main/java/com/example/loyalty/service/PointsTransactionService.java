package com.example.loyalty.service;

import com.example.loyalty.entity.PointsTransaction;
import com.example.loyalty.repository.PointsTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointsTransactionService {

    @Autowired
    private PointsTransactionRepository pointsTransactionRepository;

    public List<PointsTransaction> getCustomerTransactions(String tenantId, String customerId) {
        return pointsTransactionRepository.findByTenantIdAndCustomerId(tenantId, customerId);
    }

    public PointsTransaction saveTransaction(PointsTransaction transaction) {
        return pointsTransactionRepository.save(transaction);
    }
}