package com.elevata.marketplace.validations;

import com.elevata.marketplace.validations.validators.ZipcodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ZipcodeValidator.class)
public @interface ValidZipcode {
    String message() default "Must be a valid zipcode";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
