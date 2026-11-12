package com.elevata.marketplace.stock.eventhander;

import com.elevata.marketplace.stock.StockService;
import com.elevata.marketplace.stock.StockUnitsUpdate;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
class StockEventHandler {
    private final StockService stockService;

    StockEventHandler(StockService stockService) {
        this.stockService = stockService;
    }

    @ApplicationModuleListener
    void handle(StockUnitsUpdate stock) {
        stockService.updateUnits(stock.stockId(), stock.units());
    }
}
