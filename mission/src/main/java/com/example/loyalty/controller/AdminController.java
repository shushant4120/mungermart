package com.example.loyalty.controller;

import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final MongoTemplate mongoTemplate;

    public AdminController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Simple DB health endpoint. Returns { status: "UP" } when Mongo responds to
     * ping.
     */
    @GetMapping("/health-db")
    public ResponseEntity<Map<String, Object>> healthDb() {
        Map<String, Object> resp = new HashMap<>();
        try {
            Document result = mongoTemplate.getDb().runCommand(new Document("ping", 1));
            resp.put("status", "UP");
            resp.put("mongo", result);
            return ResponseEntity.ok(resp);
        } catch (Exception ex) {
            resp.put("status", "DOWN");
            resp.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(resp);
        }
    }
}
