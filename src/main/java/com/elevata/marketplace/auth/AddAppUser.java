package com.elevata.marketplace.auth;

import com.elevata.marketplace.auth.validations.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddAppUser(
        @NotNull @NotBlank
        String fullName,
        @NotNull @NotBlank @ValidEmail
        String email,
//        @NotNull @NotBlank @ValidPhoneNumber
        String phoneNumber,
        @NotNull @NotBlank
        String role
) {
}
