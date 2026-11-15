package com.elevata.marketplace.validations;

import com.elevata.marketplace.validations.validators.ZipcodeValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.modulith.test.ApplicationModuleTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ApplicationModuleTest
@AutoConfigureMockMvc
class ZipcodeValidatorTests {

    private ZipcodeValidator validator;

    @Mock
    private ConstraintValidatorContext validatorContext;

    @BeforeEach
    void setUp() {
        validator = new ZipcodeValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "110001", "110003", "122001", "141001", "160001", "171001",
            "180001", "226001", "248001", "302001", "380001", "395001",
            "400001", "411001", "403001", "462001", "492001", "500001",
            "520001", "560001", "600001", "682001", "700001", "751001",
            "781001", "800001", "834001"
    })
    void shouldAcceptValidZipcode(String zipcode) {
        assertTrue(validator.isValid(zipcode, validatorContext), "Invalid zipcode format.");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12345", "1234567", "000000",
            "012345", "abcdef", "12ab56",
            "12@456", "12 456", "123-456",
            "12345.6", "------", ""
    })
    void shouldRejectInvalidZipcode(String zipcode) {
        assertFalse(validator.isValid(zipcode, validatorContext));
    }

    @Test
    void shouldRejectNullZipcode() {
        assertTrue(validator.isValid(null, validatorContext));
    }
}
