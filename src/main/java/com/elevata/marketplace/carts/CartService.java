package com.elevata.marketplace.carts;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    CartResponse addToCart(CartRequest cartRequest);

    CartResponse remove(Long cartId);

    String toOrder(Long cartId);

    List<CartResponse> findAllActive();

    String allCartsToOrder();
}
