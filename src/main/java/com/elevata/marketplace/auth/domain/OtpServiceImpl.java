package com.elevata.marketplace.auth.domain;

import com.elevata.marketplace.commons.EmailService;
import com.elevata.marketplace.user.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
class OtpServiceImpl implements OtpService {
    private static final Logger log = LoggerFactory.getLogger(OtpServiceImpl.class);
    private final AppUserService appUserService;
    private final OtpRepository otpRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public OtpServiceImpl(AppUserService appUserService, OtpRepository otpRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.appUserService = appUserService;
        this.otpRepository = otpRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public String create(String email) {
        Long appUserId = appUserService.getIdByEmail(email);
        Optional<Otp> optionalOtp = otpRepository
                .findByAppUserIdOrderByCreatedAtDesc(appUserId);

        if (optionalOtp.isPresent()) {
            Otp otp = optionalOtp.get();
            if (!otp.getExpiresAt().isAfter(now())) {
                otpRepository.delete(otp);
                log.info("Expired OTP deleted.");
            } else {
                log.info("OTP is not expired yet.");
                return "OTP is not expired yet.";
            }
        }

        String rawOtp = OtpGenerator.generateNumericOtp(6);
        String saltedHash = passwordEncoder.encode(rawOtp);

        Otp otp = new Otp(saltedHash, appUserId);
        otpRepository.save(otp);

        String text = "Your otp for login " + rawOtp;
        emailService.send(email, "Login credentials", text);

        log.info("OTP generated");
        return "Otp generated for email " + email;
    }

    @Override
    public boolean verifyOtp(OtpRequest otpRequest) {
        Long appUserId = appUserService.getIdByEmail(otpRequest.email());
        Optional<Otp> optionalOtp = otpRepository.findByAppUserIdOrderByCreatedAtDesc(appUserId);

        if (optionalOtp.isPresent()) {
            Otp otp = optionalOtp.get();
            if (otp.getExpiresAt().isAfter(now())) {
                return passwordEncoder.matches(otpRequest.otpValue(),
                        otp.getOtpValue());
            } else {
                return false;
            }
        }

        /*
            What if a user without using login endpoint triggers otp creation verify endpoint?
         */
        throw new OtpSessionNotFoundException(
                "No active verification session found for this email.");
    }
}
