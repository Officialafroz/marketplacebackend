package com.elevata.marketplace.auth.domain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface LogoutService {
    ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response);
}
