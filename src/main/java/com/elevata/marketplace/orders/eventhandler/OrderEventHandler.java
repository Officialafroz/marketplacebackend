package com.elevata.marketplace.orders.eventhandler;

import com.elevata.marketplace.carts.CartOrderRequest;
import com.elevata.marketplace.orders.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
class OrderEventHandler {

    private final OrderService orderService;

    @Autowired
    OrderEventHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApplicationModuleListener
    void handle(CartOrderRequest cartOrderRequest) {
        orderService.cartToOrder(cartOrderRequest);
    }
}
