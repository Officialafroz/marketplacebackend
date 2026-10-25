package com.elevata.marketplace.auth.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final Duration refreshTokenExpiration;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenServiceImpl(
            RefreshTokenRepository refreshTokenRepository,
            @Value("${refresh-token.expiration}") Duration refreshTokenExpiration
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    @Override
    @Transactional
    public RefreshToken createOrUpdateRefreshToken(Long appUserId) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        String newToken = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(bytes);
        Instant expiresAt = Instant.now().plus(refreshTokenExpiration);

        return refreshTokenRepository.findByAppUserId(appUserId)
                .map(existing -> {
                    existing.setToken(newToken);
                    existing.setExpiresAt(expiresAt);

                    return refreshTokenRepository.save(existing);
                })
                .orElseGet(() -> {
                    RefreshToken refreshToken = new RefreshToken(newToken,
                            expiresAt,
                            appUserId);

                    return refreshTokenRepository.save(refreshToken);
                });
    }

    @Override
    @Transactional
    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Invalid refresh token."));

        if (Instant.now().isAfter(refreshToken.getExpiresAt())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenExpiredException("Refresh token expired.");
        }

        return refreshToken;
    }

    @Override
    @Transactional
    public void deleteByUser(Long appUserId) {
        refreshTokenRepository.deleteByAppUserId(appUserId);
    }

    @Override
    @Transactional
    public void deleteByToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }
}
