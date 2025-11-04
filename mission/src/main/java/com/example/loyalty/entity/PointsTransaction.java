package com.example.loyalty.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "points_transactions")
public class PointsTransaction {
    @Id
    public String id;
    public String tenantId;
    public String customerId;
    public int change;
    public int balanceAfter;
    public String reason;
    public Instant createdAt = Instant.now();

    public PointsTransaction(){}

    public PointsTransaction(String tenantId, String customerId, int change, int balanceAfter, String reason){
        this.tenantId = tenantId;
        this.customerId = customerId;
        this.change = change;
        this.balanceAfter = balanceAfter;
        this.reason = reason;
    }
}