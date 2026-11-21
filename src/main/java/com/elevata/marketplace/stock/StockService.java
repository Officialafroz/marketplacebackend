package com.elevata.marketplace.stock;

import com.elevata.marketplace.stock.domain.AddStockForProduct;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface StockService {
    ResponseEntity<StockResponse> add(AddStockForProduct stock);

    ResponseEntity<Page<StockResponse>> getProductStockForCustomer(int pageNumber);

    StockResponse addStock(Long stockId, Integer units, Long sellerId);

    ResponseEntity<StockResponse> addStock(Long stockId, Integer units);

    void reduceStock(Long stockId, Integer units, Long sellerId);

    Page<StockResponse> getAvailableStock(int pageNumber, int catalogPageSize);
}
