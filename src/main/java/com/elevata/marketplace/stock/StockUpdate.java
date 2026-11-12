package com.elevata.marketplace.stock;

import org.jspecify.annotations.Nullable;

public record StockUpdate(
        Long stockId,
        @Nullable
        Integer units,
        @Nullable
        StockStatus stockStatus
) {
}
