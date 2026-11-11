package com.elevata.marketplace.auth.domain;

import org.springframework.stereotype.Service;

@Service
interface OtpService {

    void create(String email);

    boolean verifyOtp(OtpRequest otpRequest);
}
