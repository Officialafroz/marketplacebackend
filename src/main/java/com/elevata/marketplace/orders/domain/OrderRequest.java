package com.elevata.marketplace.orders.domain;

public record OrderRequest(
        Long catalogId,
        Long addressId,
        int quantity,
        String paymentOption
) {
}
