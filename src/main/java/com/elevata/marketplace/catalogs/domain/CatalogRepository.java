package com.elevata.marketplace.catalogs.domain;

import com.elevata.marketplace.catalogs.CatalogOrderInfo;
import com.elevata.marketplace.catalogs.CatalogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
        SELECT new com.elevata.marketplace.catalogs.CatalogResponse(
            c.catalogId, c.productId, c.stockId, c.productName, c.category, c.units,
            c.price, c.stockStatus, c.manufacturingDate, c.expiryDate, c.sellerId
        ) FROM Catalog c
        WHERE c.productName LIKE CONCAT('%', LOWER(:productName), '%')
        ORDER BY c.units DESC
    """)
    Page<CatalogResponse> search(@Param("productName") String productName,
                                 Pageable pageable);

    @Query("""
        SELECT new com.elevata.marketplace.catalogs.CatalogOrderInfo(
            c.units, c.price, c.stockId
        ) FROM Catalog c
        WHERE c.catalogId = :catalogId
    """)
    Optional<CatalogOrderInfo> getCatalogOrderInfo(@Param("catalogId") Long catalogId);
}
