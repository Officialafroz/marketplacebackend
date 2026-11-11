package com.elevata.marketplace.stock;

public record StockUpdate(
        Long stockId,
        Integer units,
        StockStatus stockStatus
) {
}
