package com.elevata.marketplace.carts.domain;

import com.elevata.marketplace.carts.CartOrderRequest;
import com.elevata.marketplace.carts.CartRequest;
import com.elevata.marketplace.carts.CartResponse;
import com.elevata.marketplace.catalogs.CatalogOrderResponse;

import java.math.BigDecimal;

class CartMapper {
    static Cart toEntity(CartRequest cartRequest, BigDecimal totalPrice,
                         Long customerId, CartStatus cartStatus, boolean inStock
    ) {
        return new Cart(cartRequest.catalogId(),
                cartRequest.quantity(),
                totalPrice,
                cartStatus,
                inStock,
                customerId);
    }

    static CartResponse toDto(Cart cart, CatalogOrderResponse catalogOrderResponse) {
        return new CartResponse(cart.getCartId(),
                catalogOrderResponse,
                cart.getQuantity(),
                cart.getTotalPrice(),
                cart.getCartStatus(),
                cart.isInStock(),
                cart.getCustomerId());
    }

    static CartOrderRequest toCartOrderDto(Cart cart, CatalogOrderResponse catalogOrderResponse) {
        return new CartOrderRequest(catalogOrderResponse,
                cart.getQuantity(),
                cart.getTotalPrice(),
                cart.getCustomerId());
    }
}
