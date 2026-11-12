package com.elevata.marketplace.orders.web.controller;

import com.elevata.marketplace.orders.OrderResponse;
import com.elevata.marketplace.orders.OrderService;
import com.elevata.marketplace.orders.domain.OrderRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/customer/order")
class OrderController {

    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    OrderResponse save(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.save(orderRequest);
    }

    @PostMapping("cancel/{orderId}")
    OrderResponse cancel(@PathVariable Long orderId) {
        return orderService.cancel(orderId);
    }
}
