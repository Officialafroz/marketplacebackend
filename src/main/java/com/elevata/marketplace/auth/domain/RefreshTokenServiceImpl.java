package com.elevata.marketplace.auth.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private static final int REFRESH_TOKEN_EXPIRATION_DAYS = 7;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    @Transactional
    public RefreshToken createOrUpdateRefreshToken(Long appUserId) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        String newToken = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(bytes);
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRATION_DAYS);

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

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
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
