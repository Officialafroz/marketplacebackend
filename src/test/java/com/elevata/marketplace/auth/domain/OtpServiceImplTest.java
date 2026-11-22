package com.elevata.marketplace.auth.domain;

import com.elevata.marketplace.commons.EmailService;
import com.elevata.marketplace.user.domain.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

@ApplicationModuleTest(
        mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES
)
class OtpServiceImplTest {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private OtpService otpService;

    @MockitoBean
    private EmailService emailService;

    /*
        Mock the email service

        ###Create
        1. For invalid user
        2. validate expired OTP
        3. Verify the rawOtp's length is 6 and does not contain alphabets
        4. Verify encoding is done successfully
        5. Verify Otp is saved successfully
        6. Verify email is sent
        7. assert otp generation message
     */

    @Test
    void shouldThrowExceptionOnUnRegisteredEmailForCreate() {
        assertThrows(UserNotFoundException.class, () -> otpService.create("testp@gmail.com"));
        assertThrows(IllegalArgumentException.class, () -> otpService.create(null));
    }

    @Test
    void shouldGenerateOtpForValidEmailForCreate() {
        String expected = "Otp generated for email lergapp@gmail.com";

        assertEquals(expected, otpService.create("lergapp@gmail.com"));
        assertNotEquals(expected, otpService.create("lergapp@gmail.com"));
    }

    /*
        ###VerifyOtp
        1. Valid email as used in login for same otp
        2. Valid email as used in login for invalid otp
        3. Valid email as used in login for expired otp
        4. Registered invalid email, same otp
        5. Registered invalid email, same invalid otp
        6. Registered invalid email, same expired otp
        7. UnRegistered email, same otp
        8. UnRegistered email, same invalid otp
        9. UnRegistered email, same expired otp
        10.Encoder matching
     */
}