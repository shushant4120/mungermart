package com.example.loyalty.controller;

import com.example.loyalty.entity.Customer;
import com.example.loyalty.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Deprecated duplicate controller kept for reference. Removed Spring MVC annotations
// so it is not registered as a bean and won't conflict with the namespaced
// `com.example.loyalty.customer.controller.CustomerController`.
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer c) {
        Customer saved = customerService.createCustomer(c);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> list() {
        return ResponseEntity.ok(customerService.list());
    }

    @GetMapping("/card/{cardId}")
    public ResponseEntity<Customer> byCard(@PathVariable String cardId) {
        Customer c = customerService.findByCardId(cardId);
        if (c == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(c);
    }
}