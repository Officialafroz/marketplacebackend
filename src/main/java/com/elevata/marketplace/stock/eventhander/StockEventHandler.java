package com.elevata.marketplace.stock.eventhander;

import com.elevata.marketplace.stock.AddStock;
import com.elevata.marketplace.stock.ReduceStock;
import com.elevata.marketplace.stock.StockService;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
class StockEventHandler {
    private final StockService stockService;

    StockEventHandler(StockService stockService) {
        this.stockService = stockService;
    }

    @ApplicationModuleListener
    void handle(ReduceStock stock) {
        stockService.reduceStock(stock.stockId(), stock.units(), stock.sellerId());
    }

    @ApplicationModuleListener
    void handle(AddStock stock) {
        stockService.addStock(stock.stockId(), stock.units(), stock.sellerId());
    }
}
