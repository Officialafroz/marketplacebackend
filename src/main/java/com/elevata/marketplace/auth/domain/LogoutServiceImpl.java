package com.elevata.marketplace.auth.domain;

import com.elevata.marketplace.security.CurrentUser;
import com.elevata.marketplace.user.AppUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
class LogoutServiceImpl implements LogoutService {

    private static final Logger log = LoggerFactory.getLogger(LogoutServiceImpl.class);
    private final AppUserService appUserService;
    private final RefreshTokenService refreshTokenService;
    private final CookieService cookieService;
    private final CurrentUser currentUser;

    @Autowired
    public LogoutServiceImpl(AppUserService appUserService, RefreshTokenService refreshTokenService, CookieService cookieService, CurrentUser currentUser) {
        this.appUserService = appUserService;
        this.refreshTokenService = refreshTokenService;
        this.cookieService = cookieService;
        this.currentUser = currentUser;
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Long id = currentUser.id();

        if (!appUserService.validateById(id)) {
            return "Can't logout session. Invalid id " + id;
        }

        /*
            What if token is null? It will easy clear cookies and security context !!! which is
            a flaw
         */

        // Throws exception
        String refreshToken = cookieService.extractRefreshTokenFromCookie(request);

        if (refreshToken != null) {
            refreshTokenService.deleteByToken(refreshToken);
        }

        cookieService.clearCookie(response, "accessToken");
        cookieService.clearCookie(response, "refreshToken");

        SecurityContextHolder.clearContext();

        log.info("[User has been logged out] From the Logout Service");

        return "User has been logged out.";
    }
}
