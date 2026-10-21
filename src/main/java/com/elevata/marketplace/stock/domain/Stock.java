package com.elevata.marketplace.stock.domain;

import com.elevata.marketplace.stock.StockStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@DynamicInsert
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;
    private int units;
    private BigDecimal price;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;
    private Long productId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Stock() {
    }

    public Stock(int units,
                 BigDecimal price,
                 LocalDate manufacturingDate,
                 LocalDate expiryDate,
                 Long productId) {
        this.units = units;
        this.price = price;
        this.manufacturingDate = manufacturingDate;
        this.expiryDate = expiryDate;
        this.productId = productId;
    }

    public Long getStockId() {
        return stockId;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDate getManufacturingDate() {
        return manufacturingDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public Long getProductId() {
        return productId;
    }

    public StockStatus getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(StockStatus stockStatus) {
        this.stockStatus = stockStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
