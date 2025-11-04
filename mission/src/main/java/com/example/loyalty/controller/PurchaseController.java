package com.example.loyalty.controller;

import com.example.loyalty.dto.PurchaseRequest;
import com.example.loyalty.entity.Purchase;
import com.example.loyalty.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Legacy PurchaseController: removed controller annotations to avoid bean
// registration conflicts if a namespaced purchase controller exists. Kept
// class for reference.
public class PurchaseController {
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/create-purchase")
    public ResponseEntity<Purchase> createPurchase(@RequestBody PurchaseRequest req) {
        Purchase p = purchaseService.createPurchase(req);
        return ResponseEntity.ok(p);
    }
}