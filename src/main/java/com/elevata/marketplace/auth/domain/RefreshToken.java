package com.elevata.marketplace.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@DynamicInsert
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;
    private String token;
    private LocalDateTime expiresAt;
    private Long appUserId;
    private LocalDateTime createdAt;

    public RefreshToken(String token, LocalDateTime expiresAt, Long appUserId) {
        this.token = token;
        this.expiresAt = expiresAt;
        this.appUserId = appUserId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
