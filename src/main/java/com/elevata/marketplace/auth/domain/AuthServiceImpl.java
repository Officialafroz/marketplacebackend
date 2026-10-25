package com.elevata.marketplace.auth.domain;

import com.elevata.marketplace.auth.AuthService;
import com.elevata.marketplace.security.JwtProvider;
import com.elevata.marketplace.user.AddAppUser;
import com.elevata.marketplace.user.AppUserDto;
import com.elevata.marketplace.user.AppUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final ApplicationEventPublisher eventPublisher;
    private final AppUserService appUserService;
    private final OtpService otpService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final CookieService cookieService;

    @Autowired
    public AuthServiceImpl(ApplicationEventPublisher eventPublisher, AppUserService appUserService, OtpService otpService, JwtProvider jwtProvider, RefreshTokenService refreshTokenService, CookieService cookieService) {
        this.eventPublisher = eventPublisher;
        this.appUserService = appUserService;
        this.otpService = otpService;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
        this.cookieService = cookieService;
    }

    @Override
    @Transactional
    public String registerCustomer(HttpServletResponse response, AddAppUser appUser) {
        if (!appUserService.validateUser(appUser.email())) {
          log.info("[Publishing the save event]", appUser);
          eventPublisher.publishEvent(appUser);

          AppUserDto appUserDto = appUserService.findByEmail(appUser.email());
          handleToken(response, appUserDto.appUserId(), appUserDto.role().name());

          return "Account created for user.";
        }

        return "App user already exists for email " + appUser.email();
    }

    @Override
    @Transactional
    public String login(OtpEmailRequest otpEmailRequest) {
        if (!appUserService.validateUser(otpEmailRequest.email())) {
            return "User does not exist for email " + otpEmailRequest.email();
        }

        otpService.create(otpEmailRequest.email());

        return "Otp generated for email " + otpEmailRequest.email();
    }

    @Override
    public String verify(HttpServletResponse response, OtpRequest otpRequest) {
        if (!appUserService.validateUser(otpRequest.email())) {
            return "User does not exist for email " + otpRequest.email();
        }

        if (!otpService.verifyOtp(otpRequest)) {
            return "Invalid OTP";
        }

        AppUserDto appUserDto = appUserService.findByEmail(otpRequest.email());
        handleToken(response, appUserDto.appUserId(), appUserDto.role().name());

        return "User authorized.";
    }

    private void handleToken(HttpServletResponse response, Long appUserId, String role) {
        String accessToken = jwtProvider.generateAccessToken(appUserId, role);
        RefreshToken refreshToken = refreshTokenService.createOrUpdateRefreshToken(appUserId);

        cookieService.addAccessTokenCookie(response, accessToken);
        cookieService.addRefreshTokenCookie(response, refreshToken.getToken());
    }
}
