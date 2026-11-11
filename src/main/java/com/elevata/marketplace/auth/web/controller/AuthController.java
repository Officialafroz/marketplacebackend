package com.elevata.marketplace.auth.web.controller;

import com.elevata.marketplace.auth.AuthService;
import com.elevata.marketplace.auth.domain.OtpEmailRequest;
import com.elevata.marketplace.auth.domain.OtpRequest;
import com.elevata.marketplace.user.AddAppUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
class AuthController {

    private final AuthService authService;

    @Autowired
    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    String register(HttpServletResponse response,
                                   @Valid @RequestBody AddAppUser appUser) {
        return authService.register(response, appUser);
    }

    @PostMapping("login")
    String login(@Valid @RequestBody OtpEmailRequest otpEmailRequest) {
        return authService.login(otpEmailRequest);
    }

    @PostMapping("verify/otp")
    String verify(HttpServletResponse response,
                         @Valid @RequestBody OtpRequest otpRequest) {
        return authService.verify(response, otpRequest);
    }

    @PostMapping("refresh")
    String refresh(HttpServletRequest request, HttpServletResponse response) {
        return authService.refresh(request, response);
    }
}
