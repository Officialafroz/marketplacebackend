package com.elevata.marketplace.stock;

import com.elevata.marketplace.stock.domain.AddStockForProduct;
import com.elevata.marketplace.stock.domain.StockResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface StockService {
    StockResponse add(AddStockForProduct stock);

    Page<StockResponse> getProductStock(int pageNumber);

    StockResponse updatePriceOrStatus(Long stockId, Integer units, String status);
}
