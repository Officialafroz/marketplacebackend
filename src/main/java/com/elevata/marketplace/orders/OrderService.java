package com.elevata.marketplace.orders;

import com.elevata.marketplace.orders.domain.OrderRequest;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    OrderResponse save(OrderRequest orderRequest);

    OrderResponse cancel(Long orderId);
}
