package com.elevata.marketplace.auth.web;

import com.elevata.marketplace.auth.domain.OtpNotFoundException;
import com.elevata.marketplace.auth.domain.OtpSessionNotFoundException;
import com.elevata.marketplace.commons.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
class OtpExceptionHandler {

    @ExceptionHandler(OtpNotFoundException.class)
    ErrorResponse handleOtpNotFoundError(OtpNotFoundException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(OtpSessionNotFoundException.class)
    ErrorResponse handleOtpSessionNotFoundError(OtpSessionNotFoundException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }
}
