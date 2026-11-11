package com.elevata.marketplace.stock.web;

import com.elevata.marketplace.commons.ErrorResponse;
import com.elevata.marketplace.stock.domain.InvalidStockStatusException;
import com.elevata.marketplace.stock.domain.StockNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
class StockExceptionHandler {

    @ExceptionHandler(StockNotFoundException.class)
    ErrorResponse handleStockNotFoundError(StockNotFoundException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(InvalidStockStatusException.class)
    ErrorResponse handleInvalidStockError(InvalidStockStatusException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }
}
