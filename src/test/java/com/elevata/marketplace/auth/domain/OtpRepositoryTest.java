package com.elevata.marketplace.auth.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.modulith.test.ApplicationModuleTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ApplicationModuleTest(
        mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES
)
class OtpRepositoryTest {

    @Autowired
    private OtpRepository otpRepository;

    @Test
    void shouldThrowExceptionForFindByAppUserIdWithNull() {
        assertThrows(IllegalArgumentException.class, () -> otpRepository.findByAppUserId(null));
    }

    @Test
    void shouldThrowExceptionForFindByAppUserIdWithInvalidId() {
        assertThrows(EmptyResultDataAccessException.class, () -> otpRepository.findByAppUserId(101L));
    }

    @Test
    void shouldReturnObjectForFindByAppUserIdWithValidId() {
        Long appUserId = 10L;
        assertEquals(appUserId, otpRepository.findByAppUserId(10L).getAppUserId());
    }

    @Test
    void findByAppUserIdOrderByCreatedAtDescForInvalidId() {
        assertEquals(Optional.empty(), otpRepository
                .findByAppUserIdOrderByCreatedAtDesc(11L));
    }

    @Test
    void findByAppUserIdOrderByCreatedAtDescForValidIdOtpExpired() {
        assertTrue(otpRepository.findByAppUserIdOrderByCreatedAtDesc(10L)
                .get().getExpiresAt().isBefore(LocalDateTime.now()));
    }
}