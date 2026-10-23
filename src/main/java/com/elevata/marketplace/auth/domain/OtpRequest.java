package com.elevata.marketplace.auth.domain;

import com.elevata.marketplace.validations.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OtpRequest(
        @NotNull @ValidEmail
        String email,
        @NotNull @NotBlank
        String otpValue
) {
}
