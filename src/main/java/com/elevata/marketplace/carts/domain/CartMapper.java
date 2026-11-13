package com.elevata.marketplace.carts.domain;

import com.elevata.marketplace.carts.CartRequest;
import com.elevata.marketplace.carts.CartResponse;
import com.elevata.marketplace.catalogs.CatalogOrderResponse;
import com.elevata.marketplace.catalogs.CatalogResponse;

import java.math.BigDecimal;

class CartMapper {
    static Cart toEntity(CartRequest cartRequest, BigDecimal totalPrice,
                         Long customerId, CartStatus cartStatus
    ) {
        return new Cart(cartRequest.catalogId(),
                cartRequest.quantity(),
                totalPrice,
                cartStatus,
                customerId);
    }

    static CartResponse toDto(Cart cart, CatalogOrderResponse catalogOrderResponse) {
        return new CartResponse(catalogOrderResponse,
                cart.getQuantity(),
                cart.getTotalPrice(),
                cart.getCartStatus(),
                cart.getCustomerId());
    }
}
