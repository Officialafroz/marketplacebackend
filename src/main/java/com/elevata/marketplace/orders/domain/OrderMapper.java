package com.elevata.marketplace.orders.domain;

import com.elevata.marketplace.address.AddressResponse;
import com.elevata.marketplace.catalogs.CatalogOrderResponse;
import com.elevata.marketplace.orders.OrderResponse;

import java.math.BigDecimal;

class OrderMapper {

    static Order toEntity(OrderRequest orderRequest, Long id, BigDecimal totalPrice, OrderStatus orderStatus) {
        return new Order(orderRequest.catalogId(),
                orderRequest.addressId(),
                orderRequest.quantity(),
                totalPrice,
                PaymentOption.valueOf(orderRequest.paymentOption().toUpperCase()),
                orderStatus,
                id);
    }

    static OrderResponse toDto(Order order, AddressResponse addressResponse,
                               CatalogOrderResponse catalogOrderResponse
    ) {
        return new OrderResponse(order.getOrderId(),
                addressResponse,
                catalogOrderResponse,
                order.getQuantity(),
                order.getTotalPrice(),
                order.getOrderStatus(),
                order.getPaymentOption(),
                order.getCustomerId());
    }
}
