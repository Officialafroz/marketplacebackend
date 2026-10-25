package com.elevata.marketplace.security.internal;

import com.elevata.marketplace.security.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.RequiredTypeException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String ACCESS_TOKEN = "accessToken";
    private final JwtProvider jwtProvider;

    @Autowired
    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        Cookie cookie = WebUtils.getCookie(request, ACCESS_TOKEN);

        if (cookie != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            jwtProvider.parse(cookie.getValue())
                    .flatMap(this::toAuthentication)
                    .ifPresent(auth -> {
                        auth.setDetails(new WebAuthenticationDetailsSource()
                                .buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    });
        }

        //Invalid or missing token: stay unauthenticated; the entry point returns 401
        filterChain.doFilter(request, response);
    }

    private Optional<UsernamePasswordAuthenticationToken> toAuthentication(Claims claims) {
        try {
            Long appUserId = Long.parseLong(claims.getSubject());
            String role = claims.get("role", String.class);
            if (role == null) return Optional.empty();

            var details = new CustomUserDetails(appUserId, role);
            return Optional.of(new UsernamePasswordAuthenticationToken(
                    details, null, details.getAuthorities()));
        } catch (NumberFormatException | RequiredTypeException e) {
            return Optional.empty();
        }
    }
}
