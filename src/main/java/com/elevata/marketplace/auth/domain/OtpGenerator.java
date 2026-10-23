package com.elevata.marketplace.auth.domain;

import java.security.SecureRandom;

final class OtpGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();

    public OtpGenerator() {
    }

    public static String generateNumericOtp(int length) {
        if (length < 4 || length > 10) {
            throw new IllegalArgumentException("OTP Length must be between 4 and 10");
        }

        int bound = (int) Math.pow(10, length);
        int otp = RANDOM.nextInt(bound);

        return String.format("%0" + length + "d", otp);
    }
}
