package com.elevata.marketplace.product.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT new com.elevata.marketplace.product.domain.ProductResponse(
            p.productId, p.productName, p.category, p.productStatus)
        FROM Product p
    """)
    Page<ProductResponse> getProducts(Long id, Pageable pageable);
}
