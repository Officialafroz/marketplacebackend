package com.elevata.marketplace.stock.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AddStockForProduct(
        @NotNull @Positive
        Long productId,
        @NotNull @PositiveOrZero
        int units,
        @NotNull @Positive
        BigDecimal price,
        @NotNull
        LocalDate manufacturingDate,
        @NotNull
        LocalDate expiryDate
) {
}
