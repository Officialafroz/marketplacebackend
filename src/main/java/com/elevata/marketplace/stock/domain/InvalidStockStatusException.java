package com.elevata.marketplace.stock.domain;

public class InvalidStockStatusException extends RuntimeException {
    public InvalidStockStatusException(String message) {
        super(message);
    }
}
