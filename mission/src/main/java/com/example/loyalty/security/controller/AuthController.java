package com.example.loyalty.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        // Example: Replace with DB lookup or user service
        if ("admin".equals(username) && "adminpass".equals(password)) {
            // Generate JWT token
            String token = com.example.loyalty.security.util.JwtUtil.generateToken(username,
                    java.util.Collections.singletonMap("role", "ADMIN"));
            return ResponseEntity.ok(java.util.Collections.singletonMap("token", token));
        }
        return ResponseEntity.status(401).body(java.util.Collections.singletonMap("error", "Invalid credentials"));
    }
}
