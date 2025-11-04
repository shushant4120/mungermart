package com.example.loyalty.customer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "customers")
public class Customer {
    @Id
    public String id = UUID.randomUUID().toString();
    public String tenantId;
    public String cardId;
    public String name;
    public String phone;
    public String address;
    public int pointsBalance = 0;
    public Instant createdAt = Instant.now();
}