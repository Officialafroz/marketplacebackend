package com.elevata.marketplace.auth.web;

import com.elevata.marketplace.auth.domain.RefreshTokenExpiredException;
import com.elevata.marketplace.auth.domain.RefreshTokenNotFoundException;
import com.elevata.marketplace.commons.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RefreshTokenExceptionHandler {

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ErrorResponse handleRefreshTokenNotFoundError(RefreshTokenNotFoundException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ErrorResponse handleRefreshTokenExpiredError(RefreshTokenExpiredException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }
}
