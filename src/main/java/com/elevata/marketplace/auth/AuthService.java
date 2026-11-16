package com.elevata.marketplace.auth;

import com.elevata.marketplace.auth.domain.OtpEmailRequest;
import com.elevata.marketplace.auth.domain.OtpRequest;
import com.elevata.marketplace.user.AddAppUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    ResponseEntity<String> register(HttpServletResponse response, AddAppUser appUser);

    ResponseEntity<String> login(OtpEmailRequest otpEmailRequest);

    ResponseEntity<String> verify(HttpServletResponse response, OtpRequest otpRequest);

    ResponseEntity<String> refresh(HttpServletRequest request, HttpServletResponse response);
}
