package com.elevata.marketplace.carts.web.controller;

import com.elevata.marketplace.carts.CartResponse;
import com.elevata.marketplace.carts.CartService;
import com.elevata.marketplace.carts.CartRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/customer/cart")
class CartController {

    private final CartService cartService;

    @Autowired
    CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    CartResponse addToCart(@Valid @RequestBody CartRequest cartRequest) {
        return cartService.addToCart(cartRequest);
    }

    @PostMapping("remove/{cartId}")
    CartResponse remove(@PathVariable Long cartId) {
        return cartService.remove(cartId);
    }
}
