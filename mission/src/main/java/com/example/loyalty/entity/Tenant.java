package com.example.loyalty.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "tenants")
public class Tenant {
    @Id
    public String id;
    public String name;
    public String ownerEmail;
    public String apiKey;
    public int rewardRate = 1;
    public String currency = "INR";
    public Instant createdAt = Instant.now();

    public Tenant() { this.id = "TENANT-" + UUID.randomUUID().toString().substring(0,8); }
}