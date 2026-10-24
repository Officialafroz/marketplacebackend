package com.elevata.marketplace.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByAppUserId(Long appUserId);

    Optional<RefreshToken> findByToken(String token);

    void deleteByAppUserId(Long appUserId);

    void deleteByToken(String refreshToken);
}
