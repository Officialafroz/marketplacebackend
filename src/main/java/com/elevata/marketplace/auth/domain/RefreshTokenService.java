package com.elevata.marketplace.auth.domain;

import org.springframework.stereotype.Service;

@Service
public interface RefreshTokenService {
    RefreshToken createOrUpdateRefreshToken(Long appUserId);

    RefreshToken validateRefreshToken(String token);

    void deleteByUser(Long appUserId);

    void deleteByToken(String refreshToken);
}
