package com.elevata.marketplace.auth.domain;

public class OtpSessionNotFoundException extends RuntimeException {
    public OtpSessionNotFoundException(String message) {
        super(message);
    }
}
