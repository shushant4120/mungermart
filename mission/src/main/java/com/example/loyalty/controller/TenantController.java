package com.example.loyalty.controller;

import com.example.loyalty.entity.Tenant;
import com.example.loyalty.service.TenantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// Legacy TenantController: controller annotations removed so this class is not
// registered as a Spring bean. Use namespaced tenant services/controllers.
public class TenantController {
    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping("/register-tenant")
    public ResponseEntity<?> registerTenant(@RequestBody Map<String, String> body) {
        String shopName = body.get("shopName");
        String ownerEmail = body.get("ownerEmail");
        Tenant t = tenantService.register(shopName, ownerEmail);
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("tenantId", t.id);
        response.put("apiKey", t.apiKey);
        return ResponseEntity.ok(response);
    }
}