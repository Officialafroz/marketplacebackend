package com.elevata.marketplace.auth.domain;

import com.elevata.marketplace.validations.ValidEmail;
import jakarta.validation.constraints.NotNull;

public record OtpEmailRequest(
        @NotNull @ValidEmail
        String email
) {
}
