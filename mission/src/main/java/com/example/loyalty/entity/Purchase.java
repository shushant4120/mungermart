package com.example.loyalty.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "purchases")
public class Purchase {
    @Id
    public String id;
    public String tenantId;
    public String customerId;
    public List<PurchaseItem> items;
    public double totalAmount;
    public int pointsEarned;
    public int pointsRedeemed;
    public Instant createdAt = Instant.now();
}