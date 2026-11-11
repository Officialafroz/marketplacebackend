package com.elevata.marketplace.catalogs.domain;

import com.elevata.marketplace.catalogs.CatalogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface CatalogRepository extends JpaRepository<Catalog, Long> {
    Optional<Catalog> findByStockId(Long stockId);

    @Query("""
        SELECT new com.elevata.marketplace.catalogs.CatalogResponse(
            c.catalogId, c.productId, c.stockId, c.productName, c.category, c.units,
            c.price, c.stockStatus, c.manufacturingDate, c.expiryDate, c.sellerId
        ) FROM Catalog c
    """)
    Page<CatalogResponse> findAllResponses(Pageable pageable);
}
