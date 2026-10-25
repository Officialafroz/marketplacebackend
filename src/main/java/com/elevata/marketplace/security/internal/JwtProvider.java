package com.elevata.marketplace.security.internal;

import com.elevata.marketplace.user.domain.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtProvider {

    private final SecretKey key;
    private final Duration accessTokenTtl;

    public JwtProvider(@Value("${jwt.secret-key}") String secretKey,
                       @Value("${jwt.access-token-ttl:10}") Duration accessTokenTtl
    ) {
        // Fails at startup if the secret is invalid or too short (< 256 bits)
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.accessTokenTtl = accessTokenTtl;
    }

    public String generateAccessToken(Long appUserId, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(String.valueOf(appUserId))
                .claim("role", role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(accessTokenTtl)))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Optional<Claims> parse(String token) {
        try {
            return Optional.of(Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload());
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public boolean validateToken(String token) {
        return parse(token).isPresent();
    }
}
