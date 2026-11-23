package com.elevata.marketplace.validations;

import com.elevata.marketplace.validations.validators.PhoneNumberValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.modulith.test.ApplicationModuleTest;

import static org.junit.jupiter.api.Assertions.*;

@ApplicationModuleTest
public class PhoneNumberValidatorTests {

    private PhoneNumberValidator validator;
    private ConstraintValidatorContext validatorContext;

    @BeforeEach
    void setUp() {
        validator = new PhoneNumberValidator();
        validatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "9876543210", "8765432109", "7654321098", "6543210987",
            "+919876543210", "+91-9876543210", "+91 9876543210", "919876543210",
            "91-9876543210", "91 9876543210", "09876543210", "9123456789",
            "8123456789", "7123456789", "6123456789"
    })
    void shouldReturnTrueONValidIndianPhoneNumbers(String phoneNumber) {
        assertTrue(validator.isValid(phoneNumber, validatorContext));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1234567890", "5234567890", "4234567890", "3234567890",
            "2234567890", "0123456789", "987654321", "98765432101",
            "98765432", "987654321012", "abcdefghij", "98765abc10",
            "98765@3210", "98765.43210", "98765_43210", "+91987654321",
            "+9198765432101", "+92-9876543210", "+1-9876543210", "+91--9876543210",
            "+91  9876543210", "91+9876543210", "00919876543210", "9876 543210",
            "98765-43210", " 9876543210", "9876543210 "
    })
    void shouldReturnFalseOnInvalidIndianPhoneNumbers(String phoneNumber) {
        assertFalse(validator.isValid(phoneNumber, validatorContext));
    }

    @Test
    void shouldReturnFalseOnNullValue() {
        assertTrue(validator.isValid(null, validatorContext));
    }
}
