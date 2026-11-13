package com.elevata.marketplace.carts;

import jakarta.validation.constraints.Positive;

public record CartRequest(
        @Positive
        Long catalogId,
        @Positive
        int quantity
) {
}
