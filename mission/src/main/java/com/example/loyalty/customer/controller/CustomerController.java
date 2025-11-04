package com.example.loyalty.customer.controller;

import com.example.loyalty.customer.entity.Customer;
import com.example.loyalty.customer.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create-customer")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer c) {
        Customer saved = customerService.createCustomer(c);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/list-customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.list());
    }

    @GetMapping("/get-customer-by-card/{cardId}")
    public ResponseEntity<Customer> getCustomerByCard(@PathVariable String cardId) {
        Customer c = customerService.findByCardId(cardId);
        if (c == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(c);
    }
}