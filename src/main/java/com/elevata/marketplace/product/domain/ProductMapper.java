package com.elevata.marketplace.product.domain;

import com.elevata.marketplace.product.ProductResponse;

public final class ProductMapper {
    public ProductMapper() {
    }

    public static ProductResponse toDto(Product p) {
        return new ProductResponse(p.getProductId(),
                p.getProductName(),
                p.getCategory(),
                p.getProductStatus() == null ?
                        ProductStatus.UNAVAILABLE : p.getProductStatus());
    }

    public static Product toEntity(AddProduct product) {
        return new Product(product.productName(), product.category());
    }
}
