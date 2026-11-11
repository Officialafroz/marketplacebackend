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

    static CatalogResponse toDto(Catalog catalog) {
        return new CatalogResponse(catalog.getCatalogId(),
                catalog.getProductId(),
                catalog.getStockId(),
                catalog.getProductName(),
                catalog.getCategory(),
                catalog.getUnits(),
                catalog.getPrice(),
                catalog.getStockStatus(),
                catalog.getManufacturingDate(),
                catalog.getExpiryDate(),
                catalog.getSellerId());
    }
}
