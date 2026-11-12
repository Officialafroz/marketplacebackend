package com.elevata.marketplace.orders.web;

import com.elevata.marketplace.commons.ErrorResponse;
import com.elevata.marketplace.orders.domain.OrderNotFoundException;
import com.elevata.marketplace.orders.domain.OrderQuantityLimitReachedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
class OrderExceptionHandler {

    @ExceptionHandler(OrderQuantityLimitReachedException.class)
    ErrorResponse handleOrderQuantityLimitReachedError(OrderQuantityLimitReachedException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    ErrorResponse handleOrderNotFoundError(OrderNotFoundException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }
}
