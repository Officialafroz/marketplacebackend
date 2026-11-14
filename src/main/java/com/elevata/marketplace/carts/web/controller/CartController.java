package com.elevata.marketplace.carts.web.controller;

import com.elevata.marketplace.carts.CartResponse;
import com.elevata.marketplace.carts.CartService;
import com.elevata.marketplace.carts.CartRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("list/active")
    List<CartResponse> findAll() {
        return cartService.findAllActive();
    }

    @PostMapping("remove/{cartId}")
    CartResponse remove(@PathVariable Long cartId) {
        return cartService.remove(cartId);
    }

    @PostMapping("purchase/{cartId}")
    String toOrder(@PathVariable Long cartId) {
        return cartService.toOrder(cartId);
    }

    @PostMapping("purchase/all")
    String allCartsToOrder() {
        return cartService.allCartsToOrder();
    }
}
