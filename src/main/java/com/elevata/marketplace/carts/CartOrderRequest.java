package com.elevata.marketplace.carts;

import com.elevata.marketplace.catalogs.CatalogOrderResponse;

import java.math.BigDecimal;

public record CartOrderRequest(
        CatalogOrderResponse catalogOrderResponse,
        int quantity,
        BigDecimal totalPrice,
        Long customerId
) {
}
