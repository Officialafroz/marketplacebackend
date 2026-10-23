package com.elevata.marketplace.auth.web.controller;

import com.elevata.marketplace.auth.AddAppUser;
import com.elevata.marketplace.auth.AuthService;
import com.elevata.marketplace.auth.domain.UserResponse;
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
    public UserResponse registerCustomer(@Valid @RequestBody AddAppUser appUser) {
        return authService.registerCustomer(appUser);
    }
}
