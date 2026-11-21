package com.elevata.marketplace.orders.web.controller;

import com.elevata.marketplace.orders.OrderResponse;
import com.elevata.marketplace.orders.OrderService;
import com.elevata.marketplace.orders.domain.OrderRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/customer/order")
class OrderController {

    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    ResponseEntity<OrderResponse> save(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.save(orderRequest);
    }

    @GetMapping("list/active")
    ResponseEntity<List<OrderResponse>> findAll() {
        return orderService.findAllActive();
    }

    /// Ensure canceled ordered cannot be used again
    @PostMapping("cancel/{orderId}")
    ResponseEntity<OrderResponse> cancel(@PathVariable Long orderId) {
        return orderService.cancel(orderId);
    }
}
