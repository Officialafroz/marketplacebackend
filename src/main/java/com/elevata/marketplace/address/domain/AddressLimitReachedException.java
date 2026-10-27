package com.elevata.marketplace.address.domain;

public class AddressLimitReachedException extends RuntimeException {
    public AddressLimitReachedException(String message) {
        super(message);
    }
}
