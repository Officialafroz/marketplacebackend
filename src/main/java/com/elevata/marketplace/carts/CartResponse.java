package com.elevata.marketplace.carts;

import com.elevata.marketplace.carts.domain.CartStatus;
import com.elevata.marketplace.catalogs.CatalogOrderResponse;

import java.math.BigDecimal;

public record CartResponse(
        CatalogOrderResponse catalogOrderResponse,
        int quantity,
        BigDecimal totalPrice,
        CartStatus cartStatus,
        Long customerId
) {
}
