package com.elevata.marketplace.carts;

import java.util.List;

public record BulkCartOrderRequest(List<CartOrderRequest> orderRequest) {
}
