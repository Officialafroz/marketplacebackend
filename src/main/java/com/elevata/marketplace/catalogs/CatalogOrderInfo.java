package com.elevata.marketplace.catalogs;

import java.math.BigDecimal;

public record CatalogOrderInfo(
        int quantity,
        BigDecimal price,
        Long stockId
) {
}
