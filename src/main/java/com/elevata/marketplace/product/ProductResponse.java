package com.elevata.marketplace.product.domain;

public record ProductResponse(
        Long productId,
        String productName,
        String category,
        ProductStatus productStatus
) {
}
