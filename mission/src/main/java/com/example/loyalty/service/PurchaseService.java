package com.example.loyalty.service;

import com.example.loyalty.dto.PurchaseItemDto;
import com.example.loyalty.dto.PurchaseRequest;
import com.example.loyalty.entity.*;
import com.example.loyalty.customer.entity.Customer;
import com.example.loyalty.multitenant.TenantContext;
import com.example.loyalty.customer.repository.CustomerRepository;
import com.example.loyalty.product.repository.ProductRepository;
import com.example.loyalty.repository.PurchaseRepository;
import com.example.loyalty.repository.PointsTransactionRepository;
// Legacy PurchaseService: removed @Service annotation to avoid bean duplication
// with any namespaced purchase service implementation.
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class PurchaseService {
    private final CustomerRepository customerRepo;
    private final ProductRepository productRepo;
    private final PurchaseRepository purchaseRepo;
    private final PointsTransactionRepository pointsRepo;

    private final int POINTS_PER_100 = 1;

    public PurchaseService(CustomerRepository c, ProductRepository p, PurchaseRepository pr,
            PointsTransactionRepository pt) {
        this.customerRepo = c;
        this.productRepo = p;
        this.purchaseRepo = pr;
        this.pointsRepo = pt;
    }

    @Transactional
    public Purchase createPurchase(PurchaseRequest req) {

        String tenantId = TenantContext.getTenantId();
        Customer customer = customerRepo.findByTenantIdAndCardId(tenantId, req.cardId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        double total = 0;
        List<PurchaseItem> items = new ArrayList<>();
        for (PurchaseItemDto it : req.items) {
            Product prod = productRepo.findById(it.productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            if (prod.stock < it.qty)
                throw new RuntimeException("Not enough stock for " + prod.name);
            prod.stock = prod.stock - it.qty;
            productRepo.save(prod);

            PurchaseItem pi = new PurchaseItem();
            pi.productId = it.productId;
            pi.qty = it.qty;
            pi.unitPrice = prod.price;
            items.add(pi);

            total += it.qty * prod.price;
        }

        int pointsEarned = (int) (total / 100) * POINTS_PER_100;
        int pointsRedeemed = Math.min(req.redeemPoints, customer.pointsBalance);
        double redeemValue = pointsRedeemed * 0.5;
        double finalTotal = total - redeemValue;

        customer.pointsBalance = customer.pointsBalance - pointsRedeemed + pointsEarned;
        customerRepo.save(customer);

        Purchase purchase = new Purchase();
        purchase.tenantId = tenantId;
        purchase.customerId = customer.id;
        purchase.items = items;
        purchase.totalAmount = finalTotal;
        purchase.pointsEarned = pointsEarned;
        purchase.pointsRedeemed = pointsRedeemed;
        purchaseRepo.save(purchase);

        PointsTransaction tx1 = new PointsTransaction(tenantId, customer.id, -pointsRedeemed, customer.pointsBalance,
                "redeem at purchase");
        PointsTransaction tx2 = new PointsTransaction(tenantId, customer.id, pointsEarned, customer.pointsBalance,
                "earned from purchase");
        pointsRepo.save(tx1);
        pointsRepo.save(tx2);

        return purchase;
    }
}