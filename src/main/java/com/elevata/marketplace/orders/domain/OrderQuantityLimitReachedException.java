package com.elevata.marketplace.orders.domain;

public class OrderQuantityLimitReachedException extends RuntimeException {
    public OrderQuantityLimitReachedException(String message) {
        super(message);
    }
}
