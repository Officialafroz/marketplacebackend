package com.elevata.marketplace.auth.domain;

import com.elevata.marketplace.commons.EmailService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ApplicationModuleTest(
        mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES
)
class OtpServiceImplMockTest {

    @MockitoBean
    private OtpRepository otpRepository;

    @Autowired
    private OtpService otpService;

    @MockitoBean
    private EmailService emailService;

    @MockitoBean
    private PasswordEncoder encoder;

    /*
        Mock the email service

        ###Create
        4. Verify encoding is done successfully
        Done 5. Verify Otp is saved successfully and values are mapped
        6. Verify email is sent
     */

    @Test
    void verifyOtpSavedSuccessfullyAndValuesAreMapped() {
        String email = "test1@gmail.com";
        Long appUserId = 10L;

        String rawOtp = OtpGenerator.generateNumericOtp(6);
        String hashedOtp = encoder.encode(rawOtp);
        Otp otp = new Otp(hashedOtp, appUserId);
        when(otpRepository.save(otp)).thenReturn(otp);

        otpService.create(email);
        ArgumentCaptor<Otp> captor = ArgumentCaptor.forClass(Otp.class);
        verify(otpRepository, times(1)).save(captor.capture());

        Otp o = captor.getValue();
        assertEquals(otp.getOtpId(), o.getOtpId());
        //How these came to be equal?
        assertEquals(otp.getOtpValue(), o.getOtpValue());
        //How this is failing?
        assertTrue(encoder.matches(rawOtp, otp.getOtpValue()));
        assertNotEquals(otp.getExpiresAt(), o.getExpiresAt());
        assertEquals(otp.getAppUserId(), o.getAppUserId());
    }

    @Disabled
    @Test
    void verifyOtpWithValidEmailAndOtp() {
        Long appUserId = 10L;
        String email = "test1@gmail.com";

        otpService.create(email);
        ArgumentCaptor<Otp> captor = ArgumentCaptor.forClass(Otp.class);
        verify(otpRepository, times(1)).save(captor.capture());

        Otp otp = captor.getValue();

        OtpRequest request = new OtpRequest(email, "009348");
        assertTrue(otpService.verifyOtp(request));
    }
}