package com.elevata.marketplace.catalogs.eventhandlers;

import com.elevata.marketplace.catalogs.CatalogService;
import com.elevata.marketplace.stock.StockResponse;
import com.elevata.marketplace.stock.StockStatus;
import com.elevata.marketplace.stock.StockUnitsUpdate;
import com.elevata.marketplace.stock.StockUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
class CatalogEventHandler {

    private static final Logger log = LoggerFactory.getLogger(CatalogEventHandler.class);
    private final CatalogService catalogService;

    CatalogEventHandler(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @ApplicationModuleListener
    void handle(StockResponse stockResponse) {
        log.info("[From the catalog event handler] create event called: {}", stockResponse);
        catalogService.create(stockResponse);
    }

    @ApplicationModuleListener
    void handle(StockUpdate stockUpdate) {
        log.info("[From the catalog event handler] update event called");
        catalogService.updateUnitsOrStatus(stockUpdate);
    }

    @ApplicationModuleListener
    void handle(StockUnitsUpdate stock) {
        catalogService.updateUnits(stock.stockId(), stock.units());
    }
}
