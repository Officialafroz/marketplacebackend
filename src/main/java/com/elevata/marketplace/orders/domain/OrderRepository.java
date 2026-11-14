package com.elevata.marketplace.orders.domain;

import com.elevata.marketplace.orders.OrderResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomerIdAndOrderStatus(Long id, OrderStatus orderStatus);
}
