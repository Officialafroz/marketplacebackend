package com.elevata.marketplace.auth.domain;

import org.junit.jupiter.api.RepeatedTest;
import org.springframework.modulith.test.ApplicationModuleTest;

import static org.junit.jupiter.api.Assertions.*;

@ApplicationModuleTest(
        mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES
)
class OtpGeneratorTest {

    @RepeatedTest(50)
    void verifyTheLengthOfOtp() {
        String rawOtp = OtpGenerator.generateNumericOtp(6);

        Integer.parseInt(rawOtp);
        assertEquals(6, rawOtp.length());
    }
}