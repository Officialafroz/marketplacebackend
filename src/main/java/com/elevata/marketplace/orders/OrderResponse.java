package com.elevata.marketplace.orders;

import com.elevata.marketplace.address.AddressResponse;
import com.elevata.marketplace.catalogs.CatalogOrderResponse;
import com.elevata.marketplace.orders.domain.OrderStatus;
import com.elevata.marketplace.orders.domain.PaymentOption;

import java.math.BigDecimal;

public record OrderResponse(
        Long orderId,
        AddressResponse addressResponse,
        CatalogOrderResponse catalogOrderResponse,
        int quantity,
        BigDecimal totalPrice,
        OrderStatus orderStatus,
        PaymentOption paymentOption,
        Long customerId
) {
}
