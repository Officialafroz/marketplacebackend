package com.elevata.marketplace.stock;

import com.elevata.marketplace.stock.domain.AddStockForProduct;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface StockService {
    ResponseEntity<StockResponse> add(AddStockForProduct stock);

    ResponseEntity<Page<StockResponse>> getProductStock(int pageNumber);

    ResponseEntity<StockResponse> updateUnitsOrStatus(Long stockId, @Nullable Integer units, @Nullable String status);

    void updateUnits(Long stockId, Integer units);

    Page<StockResponse> getAvailableStock(int pageNumber, int catalogPageSize);
}
