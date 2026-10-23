package com.elevata.marketplace.auth.domain;

import com.elevata.marketplace.auth.AuthService;
import com.elevata.marketplace.user.AddAppUser;
import com.elevata.marketplace.user.AppUserService;
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

    @Autowired
    public AuthServiceImpl(ApplicationEventPublisher eventPublisher, AppUserService appUserService, OtpService otpService) {
        this.eventPublisher = eventPublisher;
        this.appUserService = appUserService;
        this.otpService = otpService;
    }

    @Override
    @Transactional
    public String registerCustomer(AddAppUser appUser) {
        if (!appUserService.validateUser(appUser.email())) {
          log.info("[Publishing the save event]", appUser);
          eventPublisher.publishEvent(appUser);
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
    public String verify(OtpRequest otpRequest) {
        if (!appUserService.validateUser(otpRequest.email())) {
            return "User does not exist for email " + otpRequest.email();
        }

        if (!otpService.verifyOtp(otpRequest)) {
            return "Invalid OTP";
        }

        return "User authorized.";
    }
}
