package com.elevata.marketplace.address.web;

import com.elevata.marketplace.commons.ErrorResponse;
import com.elevata.marketplace.address.domain.AddressLimitReachedException;
import com.elevata.marketplace.address.domain.AddressNotFoundException;
import com.elevata.marketplace.address.domain.InvalidCountryException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class AddressExceptionHandler {

    @ExceptionHandler(InvalidCountryException.class)
    public ErrorResponse handleInvalidCountryError(InvalidCountryException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ErrorResponse handleAddressNotFoundError(AddressNotFoundException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(AddressLimitReachedException.class)
    public ErrorResponse handleAddressLimitReachedError(AddressLimitReachedException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }
}
