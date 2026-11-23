package com.elevata.marketplace.validations.validators;

import com.elevata.marketplace.validations.ValidPhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    private final static Pattern PHONE_NUMBER_REGEX = Pattern
            .compile("^(?:\\+91[\\-\\s]?|91[\\-\\s]?|0)?[6-9]\\d{9}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return PHONE_NUMBER_REGEX.matcher(value).matches();
    }
}
