package com.elevata.marketplace.commons.exceptions;

import com.elevata.marketplace.commons.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
class CommonExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    ErrorResponse handleDataIntegrityViolationError(DataIntegrityViolationException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ErrorResponse handleMethodArgumentNotValidError(MethodArgumentNotValidException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    ErrorResponse handleInvalidDataAccessResourceUsageError(InvalidDataAccessResourceUsageException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ErrorResponse handleConstraintViolationError(ConstraintViolationException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(OutOfStockException.class)
    ErrorResponse handleOutOfStockError(OutOfStockException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }
}
