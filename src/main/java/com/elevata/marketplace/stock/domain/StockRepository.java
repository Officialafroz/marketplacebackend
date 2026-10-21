package com.elevata.marketplace.stock.domain;

import com.elevata.marketplace.stock.StockResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query("""
        SELECT new com.elevata.marketplace.stock.StockResponse(
            s.stockId, s.productId, s.units, s.price, s.manufacturingDate,
            s.expiryDate, s.stockStatus, s.createdAt, s.updatedAt
        ) FROM Stock s
    """)
    Page<StockResponse> getProductStock(Pageable pageable);

    @Query("""
        SELECT new com.elevata.marketplace.stock.StockResponse(
            s.stockId, s.productId, s.units, s.price, s.manufacturingDate,
            s.expiryDate, s.stockStatus, s.createdAt, s.updatedAt
        ) FROM Stock s
        WHERE s.stockStatus = 'AVAILABLE'
    """)
    Page<StockResponse> getAvailableStock(Pageable pageable);
}
