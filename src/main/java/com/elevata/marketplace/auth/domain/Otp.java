package com.elevata.marketplace.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@DynamicInsert
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long otpId;
    private String otpValue;
    private Long appUserId;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public Otp() {
    }

    public Otp(String otpValue, Long appUserId) {
        this.otpValue = otpValue;
        this.appUserId = appUserId;
        this.expiresAt = LocalDateTime.now().plusMinutes(1);
    }

    public Long getOtpId() {
        return otpId;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getOtpValue() {
        return otpValue;
    }

    public Long getAppUserId() {
        return appUserId;
    }
}
