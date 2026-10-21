package com.elevata.marketplace.catalogs;

import com.elevata.marketplace.stock.StockStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CatalogResponse(
        Long productId,
        Long stockId,
        String productName,
        String category,
        int quantity,
        BigDecimal price,
        StockStatus stockStatus,
        LocalDate manufacturingDate,
        LocalDate expiryDate
) {
}
