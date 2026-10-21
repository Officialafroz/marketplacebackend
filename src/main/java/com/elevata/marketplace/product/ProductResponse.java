package com.elevata.marketplace.product;

import com.elevata.marketplace.product.domain.ProductStatus;

public record ProductResponse(
        Long productId,
        String productName,
        String category,
        ProductStatus productStatus
) {
}
