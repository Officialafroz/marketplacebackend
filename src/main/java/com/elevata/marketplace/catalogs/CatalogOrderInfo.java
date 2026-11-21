package com.elevata.marketplace.catalogs;

import com.elevata.marketplace.stock.StockStatus;

import java.math.BigDecimal;

public record CatalogOrderInfo(
        int quantity,
        BigDecimal price,
        Long stockId,
        StockStatus stockStatus,
        Long sellerId
) {
}
