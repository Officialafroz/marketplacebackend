package com.elevata.marketplace.catalogs.domain;

import com.elevata.marketplace.catalogs.CatalogResponse;
import com.elevata.marketplace.catalogs.CatalogService;
import com.elevata.marketplace.product.ProductResponse;
import com.elevata.marketplace.product.ProductService;
import com.elevata.marketplace.stock.StockResponse;
import com.elevata.marketplace.stock.StockService;
import com.elevata.marketplace.stock.StockStatus;
import com.elevata.marketplace.stock.StockUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
class CatalogServiceImpl implements CatalogService {
    private static final int CATALOG_PAGE_SIZE = 50;
    private static final Logger log = LoggerFactory.getLogger(CatalogServiceImpl.class);
    private final StockService stockService;
    private final ProductService productService;
    private final CatalogRepository catalogRepository;

    @Autowired
    public CatalogServiceImpl(StockService stockService, ProductService productService, CatalogRepository catalogRepository) {
        this.stockService = stockService;
        this.productService = productService;
        this.catalogRepository = catalogRepository;
    }

    @Override
    public Page<CatalogResponse> getCatalogs(int pageNumber) {
        return catalogRepository.findAllResponses(PageRequest.of(pageNumber, CATALOG_PAGE_SIZE));
    }

    @Override
    public void create(StockResponse stockResponse) {
        ProductResponse productResponse = productService.findById(stockResponse.productId());

        Catalog catalog = CatalogMapper.toEntity(productResponse, stockResponse);
        catalogRepository.save(catalog);

        log.info("[Catalog has been saved]: Catalog created");
    }

    @Override
    public void updateUnitsOrStatus(StockUpdate stockUpdate) {
        Catalog catalog = catalogRepository.findByStockId(stockUpdate.stockId())
                .orElseThrow(() -> new CatalogNotFoundException("Catalog not found for stock id " 
                        + stockUpdate.stockId()));

        byte count = 0;
        StockStatus stockStatus = null;

        if (stockUpdate.units() != null && stockUpdate.units() > 0) {
            catalog.setUnits(stockUpdate.units());
            count++;
        }

        if (stockStatus != null && (StockStatus.OUT_OF_STOCK.equals(stockUpdate.stockStatus())
                || StockStatus.AVAILABLE.equals(stockUpdate.stockStatus())
                || StockStatus.EXPIRED.equals(stockUpdate.stockStatus()))
        ) {
            catalog.setStockStatus(stockStatus);
            count++;
        }

        if (count == 0) {
            throw new RuntimeException("At least 1 field is required");
        }

        log.info("Catalog stock status: {}", stockUpdate.stockStatus());
    }
}
