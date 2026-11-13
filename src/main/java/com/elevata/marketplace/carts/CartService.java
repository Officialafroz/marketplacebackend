package com.elevata.marketplace.carts;

import org.springframework.stereotype.Service;

@Service
public interface CartService {
    CartResponse addToCart(CartRequest cartRequest);

    CartResponse remove(Long cartId);
}
