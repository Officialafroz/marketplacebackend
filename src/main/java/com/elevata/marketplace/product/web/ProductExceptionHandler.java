package com.elevata.marketplace.product.web;

import com.elevata.marketplace.commons.ErrorResponse;
import com.elevata.marketplace.product.domain.InvalidProductStatusException;
import com.elevata.marketplace.product.domain.ProductNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handleProductNotFoundError(ProductNotFoundException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(InvalidProductStatusException.class)
    public ErrorResponse handleProductNotFoundError(InvalidProductStatusException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }
}
