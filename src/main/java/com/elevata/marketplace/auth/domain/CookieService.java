package com.elevata.marketplace.auth.domain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public interface CookieService {
    void addAccessTokenCookie(HttpServletResponse response, String token);

    void addRefreshTokenCookie(HttpServletResponse response, String token);

    String extractRefreshTokenFromCookie(HttpServletRequest request);

    void clearCookie(HttpServletResponse response, String tokenType);
}
