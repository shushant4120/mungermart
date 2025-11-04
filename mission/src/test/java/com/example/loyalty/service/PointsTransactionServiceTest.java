package com.example.loyalty.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.example.loyalty.config.MongoTestConfig;

import com.example.loyalty.entity.PointsTransaction;

// Commenting out test class temporarily
/*
 * @SpringBootTest
 * 
 * @ActiveProfiles("test")
 * 
 * @Import(MongoTestConfig.class)
 * class PointsTransactionServiceTest {
 * 
 * @Autowired
 * private PointsTransactionService pointsTransactionService;
 * 
 * @Test
 * void testPointsTransactionOperations() {
 * // Create a test transaction
 * String testTenantId = "test-tenant";
 * String testCustomerId = "test-customer";
 * 
 * PointsTransaction transaction = new PointsTransaction(
 * testTenantId,
 * testCustomerId,
 * 100, // points change
 * 100, // balance after
 * "Test transaction");
 * 
 * // Save the transaction
 * PointsTransaction savedTransaction =
 * pointsTransactionService.saveTransaction(transaction);
 * assertNotNull(savedTransaction.id,
 * "Transaction ID should not be null after save");
 * 
 * // Retrieve transactions for the customer
 * List<PointsTransaction> customerTransactions =
 * pointsTransactionService.getCustomerTransactions(testTenantId,
 * testCustomerId);
 * 
 * assertFalse(customerTransactions.isEmpty(),
 * "Customer should have at least one transaction");
 * assertTrue(customerTransactions.stream()
 * .anyMatch(t -> t.id.equals(savedTransaction.id)),
 * "The saved transaction should be found in customer transactions");
 * 
 * // Verify transaction data
 * PointsTransaction foundTransaction = customerTransactions.get(0);
 * assertEquals(100, foundTransaction.change, "Points change should match");
 * assertEquals(100, foundTransaction.balanceAfter,
 * "Balance after should match");
 * assertEquals("Test transaction", foundTransaction.reason,
 * "Transaction reason should match");
 * assertNotNull(foundTransaction.createdAt, "Created date should not be null");
 * }
 * }
 */