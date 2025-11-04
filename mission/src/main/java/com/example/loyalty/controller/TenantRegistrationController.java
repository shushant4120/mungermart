package com.example.loyalty.controller;

import com.example.loyalty.entity.Tenant;
import com.example.loyalty.repository.TenantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantRegistrationController {

    private final TenantRepository tenantRepository;

    public TenantRegistrationController(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public static class RegisterRequest {
        public String name;
        public String ownerEmail;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest req) {
        Tenant t = new Tenant();
        if (req != null) {
            t.name = req.name;
            t.ownerEmail = req.ownerEmail;
        }
        // generate a reasonably strong API key
        t.apiKey = generateApiKey();
        tenantRepository.save(t);

        Map<String, Object> resp = new HashMap<>();
        resp.put("tenantId", t.id);
        resp.put("apiKey", t.apiKey);
        resp.put("name", t.name);
        return ResponseEntity.ok(resp);
    }

    private String generateApiKey() {
        // UUID + 16 random bytes base64-url to keep it compact and strong
        String uuid = UUID.randomUUID().toString().replace("-", "");
        byte[] random = new byte[16];
        new SecureRandom().nextBytes(random);
        String suffix = Base64.getUrlEncoder().withoutPadding().encodeToString(random);
        return uuid + suffix;
    }
}
