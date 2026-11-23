package com.elevata.marketplace.stock.web.controller;

import com.elevata.marketplace.stock.StockResponse;
import com.elevata.marketplace.stock.StockService;
import com.elevata.marketplace.stock.domain.AddStockForProduct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/seller/stock")
@Validated
class StockController {
    private final StockService stockService;

    StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    ResponseEntity<StockResponse> add(@RequestBody @Valid AddStockForProduct stock) {
        return stockService.add(stock);
    }

    @GetMapping("page/{pageNumber}")
    ResponseEntity<Page<StockResponse>> getProductStockForCustomer(@PathVariable int pageNumber) {
        return stockService.getProductStockForCustomer(pageNumber);
    }

    @PatchMapping("{stockId}")
    ResponseEntity<StockResponse> addStock(
            @PathVariable Long stockId,
            @RequestParam @NotNull @PositiveOrZero Integer units
    ) {
        return stockService.addStock(stockId, units);
    }
}
