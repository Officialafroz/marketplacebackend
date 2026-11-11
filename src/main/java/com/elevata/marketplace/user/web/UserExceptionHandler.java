package com.elevata.marketplace.user.web;

import com.elevata.marketplace.commons.ErrorResponse;
import com.elevata.marketplace.user.domain.UserNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    ErrorResponse handleUserNotFoundError(UserNotFoundException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }
}
