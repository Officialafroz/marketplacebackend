package com.elevata.marketplace.stock.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query("""
        SELECT new com.elevata.marketplace.stock.domain.StockResponse(
            s.stockId, s.productId, s.units, s.price, s.manufacturingDate,
            s.expiryDate, s.stockStatus, s.createdAt, s.updatedAt
        ) FROM Stock s
    """)
    Page<StockResponse> getProductStock(Pageable pageable);
}
