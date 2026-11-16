package com.elevata.marketplace.stock.web.controller;

import com.elevata.marketplace.stock.StockResponse;
import com.elevata.marketplace.stock.StockService;
import com.elevata.marketplace.stock.domain.AddStockForProduct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/seller/stock")
class StockController {
    private final StockService stockService;

    @Autowired
    StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    ResponseEntity<StockResponse> add(@RequestBody @Valid AddStockForProduct stock) {
        return stockService.add(stock);
    }

    @GetMapping("page/{pageNumber}")
    ResponseEntity<Page<StockResponse>> getProductStock(@PathVariable int pageNumber) {
        return stockService.getProductStock(pageNumber);
    }

    @PatchMapping("{stockId}")
    ResponseEntity<StockResponse> updateUnitsOrStatus(
            @PathVariable Long stockId,
            @RequestParam(required = false) Integer units,
            @RequestParam(required = false) String status
    ) {
        return stockService.updateUnitsOrStatus(stockId, units, status);
    }
}
