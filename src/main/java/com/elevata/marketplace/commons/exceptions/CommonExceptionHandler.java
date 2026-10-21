package com.elevata.marketplace.commons.exceptions;

import com.elevata.marketplace.commons.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse handlerDataIntegrityViolationError(DataIntegrityViolationException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }
}
