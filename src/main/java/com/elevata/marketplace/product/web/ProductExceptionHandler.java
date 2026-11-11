package com.elevata.marketplace.product.web;

import com.elevata.marketplace.commons.ErrorResponse;
import com.elevata.marketplace.product.domain.InvalidProductStatusException;
import com.elevata.marketplace.product.domain.ProductNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
class ProductExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    ErrorResponse handleProductNotFoundError(ProductNotFoundException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(InvalidProductStatusException.class)
    ErrorResponse handleProductNotFoundError(InvalidProductStatusException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }
}
