package com.elevata.marketplace.auth.domain;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;

@Service
public class CookieServiceImpl implements CookieService {

    private final Duration accessTokenTtl;
    private final Duration refreshTokenExpiration;

    public CookieServiceImpl(@Value("${jwt.access-token-ttl}") Duration accessTokenTtl,
                             @Value("${refresh-token.expiration}") Duration refreshTokenExpiration
    ) {
        this.accessTokenTtl = accessTokenTtl;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    @Override
    public void addAccessTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("accessToken", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setMaxAge(Math.toIntExact(accessTokenTtl.toSeconds()));
        //toIntExact can throw Arithmetic exception if the value overflows

        response.addCookie(cookie);
    }

    @Override
    public void addRefreshTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setMaxAge(Math.toIntExact(refreshTokenExpiration.toSeconds()));

        response.addCookie(cookie);
    }

    @Override
    public String extractRefreshTokenFromCookie(HttpServletRequest request) {
        String tokenType = "refreshToken";

        return Arrays.stream(request.getCookies())
                .filter(cookie -> tokenType.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found."));
    }

    @Override
    public void clearCookie(HttpServletResponse response, String tokenType) {
        Cookie cookie = new Cookie(tokenType, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }
}
