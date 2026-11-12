package com.elevata.marketplace.stock;

import com.elevata.marketplace.stock.domain.AddStockForProduct;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface StockService {
    StockResponse add(AddStockForProduct stock);

    Page<StockResponse> getProductStock(int pageNumber);

    StockResponse updateUnitsOrStatus(Long stockId, @Nullable Integer units, @Nullable String status);

    void updateUnits(Long stockId, Integer units);

    Page<StockResponse> getAvailableStock(int pageNumber, int catalogPageSize);
}
