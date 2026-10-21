package com.elevata.marketplace.stock.web.controller;

import com.elevata.marketplace.stock.StockResponse;
import com.elevata.marketplace.stock.StockService;
import com.elevata.marketplace.stock.domain.AddStockForProduct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/seller/stock")
public class StockController {
    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public StockResponse add(@RequestBody @Valid AddStockForProduct stock) {
        return stockService.add(stock);
    }

    @GetMapping("page/{pageNumber}")
    public Page<StockResponse> getProductStock(@PathVariable int pageNumber) {
        return stockService.getProductStock(pageNumber);
    }

    @PatchMapping("{stockId}")
    public StockResponse updatePriceOrStatus(
            @PathVariable Long stockId,
            @RequestParam(required = false) Integer units,
            @RequestParam(required = false) String status
    ) {
        return stockService.updatePriceOrStatus(stockId, units, status);
    }
}
