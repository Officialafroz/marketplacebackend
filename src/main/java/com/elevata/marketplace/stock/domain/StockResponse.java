package com.elevata.marketplace.stock.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record StockResponse(
        Long stockId,
        Long productId,
        int units,
        BigDecimal price,
        LocalDate manufacturingDate,
        LocalDate expiryDate,
        StockStatus stockStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
