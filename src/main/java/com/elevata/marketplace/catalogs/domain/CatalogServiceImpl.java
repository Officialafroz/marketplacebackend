package com.elevata.marketplace.catalogs.domain;

import com.elevata.marketplace.catalogs.CatalogResponse;
import com.elevata.marketplace.catalogs.CatalogService;
import com.elevata.marketplace.product.ProductResponse;
import com.elevata.marketplace.product.ProductService;
import com.elevata.marketplace.product.domain.Product;
import com.elevata.marketplace.stock.StockResponse;
import com.elevata.marketplace.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CatalogServiceImpl implements CatalogService {
    private static final int CATALOG_PAGE_SIZE = 50;
    private final StockService stockService;
    private final ProductService productService;

    @Autowired
    public CatalogServiceImpl(StockService stockService, ProductService productService) {
        this.stockService = stockService;
        this.productService = productService;
    }

    @Override
    public Page<CatalogResponse> getCatalogs(int pageNumber) {
        Page<StockResponse> availableStock = stockService
                .getAvailableStock(pageNumber, CATALOG_PAGE_SIZE);
        List<Long> availableProductIds = availableStock.stream()
                .filter(stockResponse -> stockResponse.stockStatus().name()
                        .equals("AVAILABLE"))
                .map(StockResponse::productId)
                .toList();
        Page<ProductResponse> availableProducts = productService
                .getAvailableProducts(availableProductIds, pageNumber, CATALOG_PAGE_SIZE);

//      Convert the products into a Map for instant O(1) lookups instead of nested loops
        Map<Long, ProductResponse> productMap = availableProducts.stream()
                .collect(Collectors.toMap(ProductResponse::productId, Function.identity()));

        return availableStock.map(stockResponse -> {
            ProductResponse product = productMap.get(stockResponse.productId());
            String productName = (product != null) ? product.productName() : "Unknown product";
            String category = (product != null) ? product.category() : "Unknown category";

            return new CatalogResponse(stockResponse.productId(),
                                stockResponse.stockId(),
                                productName,
                                category,
                                stockResponse.units(),
                                stockResponse.price(),
                                stockResponse.stockStatus(),
                                stockResponse.manufacturingDate(),
                                stockResponse.expiryDate());
        });
    }
}
