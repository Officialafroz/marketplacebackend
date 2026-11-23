package com.elevata.marketplace.auth.domain;

import com.elevata.marketplace.commons.EmailService;
import com.elevata.marketplace.user.domain.UserNotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ApplicationModuleTest(
        mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES
)
class OtpServiceImplTest {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private PasswordEncoder encoder;

    @MockitoBean
    private EmailService emailService;

    /*
        Mock the email service

        ###Create
        done 1. For invalid user (email, null) and valid email
        done - What if appUserId is null (null is handled by AppUserService) ?
        done 2. validate existence (verify return message), expired OTP (verify expired otp is deleted)
        4. Verify encoding is done successfully
        6. Verify email is sent
        done 7. assert otp generation message
     */

    @Test
    void shouldThrowExceptionOnUnRegisteredEmailAndNullForCreate() {
        assertThrows(UserNotFoundException.class, () -> otpService.create("testp@gmail.com"));
        assertThrows(IllegalArgumentException.class, () -> otpService.create(null));
    }

    @Disabled
    @Test
    void verifyIdForValidEmailForCreate() {
        String expected = "Otp generated for email test1@gmail.com";

        assertEquals(expected, otpService.create("test1@gmail.com"));

        ArgumentCaptor<Otp> captor = ArgumentCaptor.forClass(Otp.class);
        verify(otpRepository, times(1)).save(captor.capture());

        Otp otp = captor.getValue();

        assertEquals(10L, otp.getAppUserId());
    }

    @Test
    void shouldGenerateOtpForValidEmailForCreate() {
        String email = "test1@gmail.com";
        String expected = "Otp generated for email " + email;

        assertEquals(expected, otpService.create(email));
        assertNotEquals(expected, otpService.create(email));
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