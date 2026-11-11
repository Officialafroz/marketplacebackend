package com.elevata.marketplace.catalogs.web;

import com.elevata.marketplace.catalogs.domain.CatalogNotFoundException;
import com.elevata.marketplace.commons.ErrorResponse;
import com.elevata.marketplace.product.domain.ProductNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
class CatalogExceptionHandler {

    @ExceptionHandler(CatalogNotFoundException.class)
    ErrorResponse handleCatalogNotFoundError(CatalogNotFoundException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }
}
