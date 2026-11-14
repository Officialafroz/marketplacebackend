package com.elevata.marketplace.orders;

import com.elevata.marketplace.carts.BulkCartOrderRequest;
import com.elevata.marketplace.carts.CartOrderRequest;
import com.elevata.marketplace.orders.domain.OrderRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    OrderResponse save(OrderRequest orderRequest);

    OrderResponse cancel(Long orderId);

    void cartToOrder(CartOrderRequest cartOrderRequest);

    void allCartsToOrder(BulkCartOrderRequest bulkCartOrderRequest);

    List<OrderResponse> findAllActive();
}
