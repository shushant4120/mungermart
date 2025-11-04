package com.example.loyalty.service;

import com.example.loyalty.entity.Customer;
import com.example.loyalty.multitenant.TenantContext;
import com.example.loyalty.repository.CustomerRepository;
// Legacy service - annotations removed to avoid bean conflicts with namespaced
// `com.example.loyalty.customer.service.CustomerService`.
import java.util.List;
import java.util.UUID;

public class CustomerService {
    private final CustomerRepository customerRepo;

    public CustomerService(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Customer createCustomer(Customer c) {
        String tenantId = TenantContext.getTenantId();
        c.tenantId = tenantId;
        if (c.cardId == null || c.cardId.isEmpty())
            c.cardId = "CUST-" + UUID.randomUUID().toString().substring(0, 8);
        return customerRepo.save(c);
    }

    public List<Customer> list() {
        return customerRepo.findByTenantId(TenantContext.getTenantId());
    }

    public Customer findByCardId(String cardId) {
        return customerRepo.findByTenantIdAndCardId(TenantContext.getTenantId(), cardId).orElse(null);
    }
}