package com.elevata.marketplace.product.domain;

import com.elevata.marketplace.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT new com.elevata.marketplace.product.ProductResponse(
            p.productId, p.productName, p.category, p.productStatus)
        FROM Product p
    """)
    Page<ProductResponse> getProducts(Long id, Pageable pageable);

    @Query("""
        SELECT new com.elevata.marketplace.product.ProductResponse(
            p.productId, p.productName, p.category, p.productStatus)
        FROM Product p
        WHERE p.productId IN :availableProductIds
    """)
    Page<ProductResponse> findAllProductsByIdUsingIn(@Param("availableProductIds")
                                                     List<Long> availableProductIds,
                                                     Pageable pageable);
}
