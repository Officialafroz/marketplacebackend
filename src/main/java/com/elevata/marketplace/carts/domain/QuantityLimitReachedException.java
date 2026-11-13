package com.elevata.marketplace.carts.domain;

public class QuantityLimitReachedException extends RuntimeException {
    public QuantityLimitReachedException(String message) {
        super(message);
    }
}
