package com.elevata.marketplace.auth.domain;

import org.springframework.stereotype.Service;

@Service
public interface OtpService {

    void create(String email);

    boolean verifyOtp(OtpRequest otpRequest);
}
