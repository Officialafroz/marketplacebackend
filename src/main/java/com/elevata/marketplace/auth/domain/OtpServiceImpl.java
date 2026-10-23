package com.elevata.marketplace.auth.domain;

import com.elevata.marketplace.commons.EmailService;
import com.elevata.marketplace.user.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OtpServiceImpl implements OtpService {
    private static final Logger log = LoggerFactory.getLogger(OtpServiceImpl.class);
    private final AppUserService appUserService;
    private final OtpRepository otpRepository;
    private final EmailService emailService;

    @Autowired
    public OtpServiceImpl(AppUserService appUserService, OtpRepository otpRepository, EmailService emailService) {
        this.appUserService = appUserService;
        this.otpRepository = otpRepository;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public void create(String email) {
        Long appUserId = appUserService.getIdByEmail(email);
        LocalDateTime expiresAt = otpRepository
                .findByAppUserIdOrderByCreatedAtDesc(appUserId).getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            String otpValue = OtpGenerator.generateNumericOtp(6);

            Otp otp = new Otp(otpValue, appUserId);
            otpRepository.save(otp);

            String text = "Your otp for login " + otpValue;
            emailService.send(email, "Login credentials", text);
            log.info("OTP generated: {}", otpValue);
            return;
        }

        log.info("OTP is not expired yet.");
    }

    @Override
    public boolean verifyOtp(OtpRequest otpRequest) {
        Long appUserId = appUserService.getIdByEmail(otpRequest.email());
        Otp otp = otpRepository.findByAppUserIdOrderByCreatedAtDesc(appUserId);

        return otp.getExpiresAt().isAfter(LocalDateTime.now())
                && otp.getOtpValue().equals(otpRequest.otpValue());
    }
}
