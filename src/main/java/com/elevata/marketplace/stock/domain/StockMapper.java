package com.elevata.marketplace.stock.domain;

import com.elevata.marketplace.stock.StockResponse;
import com.elevata.marketplace.stock.StockStatus;

public final class StockMapper {
    public StockMapper() {}

    public static StockResponse convertToDto(Stock s) {
        return new StockResponse(s.getStockId(),
                s.getProductId(),
                s.getUnits(),
                s.getPrice(),
                s.getManufacturingDate(),
                s.getExpiryDate(),
                s.getStockStatus() == null ? StockStatus.OUT_OF_STOCK : s.getStockStatus(),
                s.getCreatedAt(),
                s.getUpdatedAt());
    }

    public static Stock toEntity(AddStockForProduct stock) {
        return new Stock(stock.units(),
                stock.price(),
                stock.manufacturingDate(),
                stock.expiryDate(),
                stock.productId());
    }
}
