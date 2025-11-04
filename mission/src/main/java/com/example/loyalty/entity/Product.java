package com.example.loyalty.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "products")
public class Product {
    @Id
    public String id;
    public String tenantId;
    public String name;
    public double price;
    public int stock;
    public Instant createdAt = Instant.now();
}