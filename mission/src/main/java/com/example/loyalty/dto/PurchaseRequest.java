package com.example.loyalty.dto;

import java.util.List;

public class PurchaseRequest {
    public String cardId;
    public List<PurchaseItemDto> items;
    public int redeemPoints = 0;
}