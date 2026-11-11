package com.elevata.marketplace.catalogs.domain;

import com.elevata.marketplace.stock.StockStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@DynamicInsert
public class Catalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long catalogId;
    private Long productId;
    private String productName;
    private String category;
    private Long stockId;
    private int units;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private Long sellerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Catalog() {
    }

    public Catalog(Long productId,
                   String productName,
                   String category,
                   Long stockId,
                   int units,
                   BigDecimal price,
                   StockStatus stockStatus,
                   LocalDate manufacturingDate,
                   LocalDate expiryDate,
                   Long sellerId
    ) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.stockId = stockId;
        this.units = units;
        this.price = price;
        this.stockStatus = stockStatus;
        this.manufacturingDate = manufacturingDate;
        this.expiryDate = expiryDate;
        this.sellerId = sellerId;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public Long getStockId() {
        return stockId;
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

    public Long getSellerId() {
        return sellerId;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public StockStatus getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(StockStatus stockStatus) {
        this.stockStatus = stockStatus;
    }
}
