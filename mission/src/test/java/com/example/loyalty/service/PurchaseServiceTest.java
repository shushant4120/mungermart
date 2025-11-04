package com.example.loyalty.service;

import com.example.loyalty.dto.PurchaseItemDto;
import com.example.loyalty.dto.PurchaseRequest;
import com.example.loyalty.entity.*;
import com.example.loyalty.customer.entity.Customer;
import com.example.loyalty.multitenant.TenantContext;
import com.example.loyalty.product.repository.ProductRepository;
import com.example.loyalty.customer.repository.CustomerRepository;
import com.example.loyalty.repository.PointsTransactionRepository;
import com.example.loyalty.repository.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class PurchaseServiceTest {

    @Mock
    CustomerRepository customerRepo;
    @Mock
    ProductRepository productRepo;
    @Mock
    PurchaseRepository purchaseRepo;
    @Mock
    PointsTransactionRepository pointsRepo;

    @InjectMocks
    PurchaseService purchaseService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        TenantContext.setTenantId("T1");
    }

    @Test
    void createPurchase_happyPath_updatesStockAndPoints() {
        Customer cust = new Customer();
        cust.id = "c1";
        cust.cardId = "card1";
        cust.pointsBalance = 10;
        cust.tenantId = "T1";
        when(customerRepo.findByTenantIdAndCardId("T1", "card1")).thenReturn(Optional.of(cust));

        Product prod = new Product();
        prod.id = "p1";
        prod.name = "Tomato";
        prod.price = 50.0;
        prod.stock = 10;
        prod.tenantId = "T1";
        when(productRepo.findById("p1")).thenReturn(Optional.of(prod));

        when(productRepo.save(ArgumentMatchers.any())).thenAnswer(i -> i.getArgument(0));
        when(customerRepo.save(ArgumentMatchers.any())).thenAnswer(i -> i.getArgument(0));
        when(purchaseRepo.save(ArgumentMatchers.any())).thenAnswer(i -> i.getArgument(0));
        when(pointsRepo.save(ArgumentMatchers.any())).thenAnswer(i -> i.getArgument(0));

        PurchaseItemDto it = new PurchaseItemDto();
        it.productId = "p1";
        it.qty = 2;
        PurchaseRequest req = new PurchaseRequest();
        req.cardId = "card1";
        req.items = (java.util.List<com.example.loyalty.dto.PurchaseItemDto>) java.util.Arrays.asList(it);
        req.redeemPoints = 5;

        Purchase purchase = purchaseService.createPurchase(req);

        assertNotNull(purchase);
        // stock reduced
        assertEquals(8, prod.stock);
        // customer points updated = 10 - 5 + earned( (2*50)/100 => 1*1 ) => 6
        assertEquals(6, cust.pointsBalance);
    }
}
