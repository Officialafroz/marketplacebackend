package com.elevata.marketplace.carts.web;

import com.elevata.marketplace.carts.domain.CartNotFoundException;
import com.elevata.marketplace.carts.domain.QuantityLimitReachedException;
import com.elevata.marketplace.commons.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
class CartExceptionHandler {

    @ExceptionHandler(CartNotFoundException.class)
    ErrorResponse handleCartNotFoundError(CartNotFoundException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(QuantityLimitReachedException.class)
    ErrorResponse handleQuantityLimitReachedError(QuantityLimitReachedException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }
}
