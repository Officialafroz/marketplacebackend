package com.elevata.marketplace.auth.domain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public interface LogoutService {
    String logout(HttpServletRequest request, HttpServletResponse response, Long id);
}
