package com.elevata.marketplace.stock.web;

import com.elevata.marketplace.commons.ErrorResponse;
import com.elevata.marketplace.stock.domain.InvalidStockStatusException;
import com.elevata.marketplace.stock.domain.StockNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class StockExceptionHandler {

    @ExceptionHandler(StockNotFoundException.class)
    public ErrorResponse handleStockNotFoundError(StockNotFoundException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(InvalidStockStatusException.class)
    public ErrorResponse handleInvalidStockError(InvalidStockStatusException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }
}
