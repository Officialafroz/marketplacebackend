package com.elevata.marketplace.validations.validators;

import com.elevata.marketplace.validations.ValidZipcode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ZipcodeValidator implements ConstraintValidator<ValidZipcode, String> {
    private static final Pattern ZIPCODE_REGEX = Pattern.compile("^[1-9][0-9]{5}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return ZIPCODE_REGEX.matcher(value).matches();
    }
}
