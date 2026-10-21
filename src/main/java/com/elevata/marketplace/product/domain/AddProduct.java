package com.elevata.marketplace.product.domain;

import jakarta.validation.constraints.NotNull;

public record AddProduct(
        @NotNull
        String productName,
        @NotNull
        String category
) {
}
