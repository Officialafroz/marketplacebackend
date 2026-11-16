package com.elevata.marketplace.orders;

import com.elevata.marketplace.carts.BulkCartOrderRequest;
import com.elevata.marketplace.carts.CartOrderRequest;
import com.elevata.marketplace.orders.domain.OrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    ResponseEntity<OrderResponse> save(OrderRequest orderRequest);

    ResponseEntity<OrderResponse> cancel(Long orderId);

    void cartToOrder(CartOrderRequest cartOrderRequest);

    void allCartsToOrder(BulkCartOrderRequest bulkCartOrderRequest);

    ResponseEntity<List<OrderResponse>> findAllActive();
}
