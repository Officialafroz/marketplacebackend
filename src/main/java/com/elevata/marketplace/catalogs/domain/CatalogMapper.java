package com.elevata.marketplace.catalogs.domain;

import com.elevata.marketplace.catalogs.CatalogResponse;
import com.elevata.marketplace.product.ProductResponse;
import com.elevata.marketplace.stock.StockResponse;

class CatalogMapper {

    static Catalog toEntity(ProductResponse productResponse, StockResponse stockResponse) {
        return new Catalog(productResponse.productId(),
                productResponse.productName(),
                productResponse.category(),
                stockResponse.stockId(),
                stockResponse.units(),
                stockResponse.price(),
                stockResponse.stockStatus(),
                stockResponse.manufacturingDate(),
                stockResponse.expiryDate(),
                productResponse.sellerId());
    }
}
