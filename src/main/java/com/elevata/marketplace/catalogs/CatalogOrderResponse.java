package com.elevata.marketplace.catalogs;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CatalogOrderResponse(
        Long catalogId,
        String productName,
        String category,
        BigDecimal price,
        LocalDate manufacturingDate,
        LocalDate expiryDate,
        Long sellerId
) {
}
