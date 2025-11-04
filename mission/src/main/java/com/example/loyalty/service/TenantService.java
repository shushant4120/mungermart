package com.example.loyalty.service;

import com.example.loyalty.entity.Tenant;
import com.example.loyalty.repository.TenantRepository;
// Legacy TenantService: removed @Service annotation to avoid duplicate bean regi
// stration when a namespaced tenant service exists.

import java.util.UUID;

public class TenantService {
    private final TenantRepository tenantRepo;

    public TenantService(TenantRepository tenantRepo) {
        this.tenantRepo = tenantRepo;
    }

    public Tenant register(String shopName, String ownerEmail) {
        Tenant t = new Tenant();
        t.name = shopName;
        t.ownerEmail = ownerEmail;
        t.apiKey = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
        tenantRepo.save(t);
        return t;
    }
}