package com.elevata.marketplace.auth.web.controller;

import com.elevata.marketplace.auth.AuthService;
import com.elevata.marketplace.auth.domain.OtpEmailRequest;
import com.elevata.marketplace.auth.domain.OtpRequest;
import com.elevata.marketplace.user.AddAppUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register/customer")
    public String registerCustomer(@Valid @RequestBody AddAppUser appUser) {
        return authService.registerCustomer(appUser);
    }

    @PostMapping("login")
    public String login(@Valid @RequestBody OtpEmailRequest otpEmailRequest) {
        return authService.login(otpEmailRequest);
    }

    @PostMapping("verify/otp")
    public String verify(@Valid @RequestBody OtpRequest otpRequest) {
        return authService.verify(otpRequest);
    }
}
