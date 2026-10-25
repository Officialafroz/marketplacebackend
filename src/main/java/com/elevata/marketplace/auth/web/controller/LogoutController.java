package com.elevata.marketplace.auth.web.controller;

import com.elevata.marketplace.auth.domain.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class LogoutController {

    private final LogoutService logoutService;

    @Autowired
    public LogoutController(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    // Why passing the id?
    @GetMapping("logout/{id}")
    public String logout(HttpServletRequest request, HttpServletResponse response, @PathVariable Long id) {
        return logoutService.logout(request, response, id);
    }
}
