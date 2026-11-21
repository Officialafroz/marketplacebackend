package com.elevata.marketplace.stock;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.jspecify.annotations.Nullable;

public record CatalogUpdate(
        @NotNull
        Long sellerId,
        Long stockId,
        @NotNull @PositiveOrZero
        Integer units,
        boolean isStatusUpdated,
        @Nullable
        StockStatus stockStatus
) {
}
