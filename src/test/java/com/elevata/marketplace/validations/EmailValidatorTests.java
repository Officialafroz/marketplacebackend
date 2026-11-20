package com.elevata.marketplace.validations;

import com.elevata.marketplace.validations.validators.EmailValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.modulith.test.ApplicationModuleTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ApplicationModuleTest
class EmailValidatorTests {

    private EmailValidator validator;
    private ConstraintValidatorContext validatorContext;

    @BeforeEach
    void setUp() {
        validator = new EmailValidator();
        validatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "test@example.com",
            "alice123@yahoo.com",
            "user_name@example.com",
            "user-name@example.com",
            "first.last@company.co.in",
            "admin@localhost.com",
            "test123@mail.example.org",
            "user+test@gmail.com",
            "a.b.c@example.co"
    })
    void shouldAcceptValidEmails(String email) {
        assertTrue(validator.isValid(email, validatorContext),
                "Should be valid email format");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "plainaddress",
            "missingatsign.com",
            "user@",
            "@example.com",
            "user@.com",
            "user@example",
            "user@com",
            "user..name@example.com",
            ".user@example.com",
            "user.@example.com",
            "user name@example.com",
            "user@exam ple.com",
            "user@@example.com"
    })
    void shouldRejectInvalidEmails(String email) {
        assertFalse(validator.isValid(email, validatorContext));
    }

    @Test
    void shouldRejectNullEmail() {
        assertTrue(validator.isValid(null, validatorContext));
    }
}
