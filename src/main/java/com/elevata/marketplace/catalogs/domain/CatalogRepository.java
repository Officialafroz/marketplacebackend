package com.elevata.marketplace.catalogs.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface CatalogRepository extends JpaRepository<Catalog, Long> {
    Optional<Catalog> findByStockId(Long stockId);
}
