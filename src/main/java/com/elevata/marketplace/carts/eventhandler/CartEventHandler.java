package com.elevata.marketplace.carts.eventhandler;

import com.elevata.marketplace.carts.CartService;
import com.elevata.marketplace.catalogs.VerifyCartStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
public class CartEventHandler {

    private final CartService cartService;

    public CartEventHandler(CartService cartService) {
        this.cartService = cartService;
    }

    @ApplicationModuleListener
    void handle(VerifyCartStock verifyCartStock) {
        cartService.verifyStock(verifyCartStock);
    }
}
