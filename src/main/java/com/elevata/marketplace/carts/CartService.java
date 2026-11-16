package com.elevata.marketplace.carts;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    ResponseEntity<CartResponse> addToCart(CartRequest cartRequest);

    ResponseEntity<CartResponse> remove(Long cartId);

    ResponseEntity<String> toOrder(Long cartId);

    ResponseEntity<List<CartResponse>> findAllActive();

    ResponseEntity<String> allCartsToOrder();
}
