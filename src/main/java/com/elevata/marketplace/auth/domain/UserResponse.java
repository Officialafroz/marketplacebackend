package com.elevata.marketplace.auth.domain;

import com.elevata.marketplace.auth.AppUserRole;

import java.time.LocalDateTime;

public record UserResponse(
        String fullName,
        String email,
        String phoneNumber,
        AppUserRole role,
        AppUserStatus Status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
