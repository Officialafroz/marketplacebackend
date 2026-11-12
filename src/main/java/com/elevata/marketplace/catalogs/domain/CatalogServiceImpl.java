package com.elevata.marketplace.catalogs.domain;

import com.elevata.marketplace.catalogs.CatalogOrderInfo;
import com.elevata.marketplace.catalogs.CatalogOrderResponse;
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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void create(StockResponse stockResponse) {
        ProductResponse productResponse = productService.findById(stockResponse.productId());

        Catalog catalog = CatalogMapper.toEntity(productResponse, stockResponse);
        catalogRepository.save(catalog);
        log.info("[Catalog has been saved]: Catalog created");
    }

    @Override
    @Transactional
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

        catalogRepository.save(catalog);
        log.info("Catalog stock status: {}", stockUpdate.stockStatus());
    }

    @Override
    public Page<CatalogResponse> search(String productName, int pageNumber) {
        return catalogRepository.search(productName,
                PageRequest.of(pageNumber, CATALOG_PAGE_SIZE));
    }

    @Override
    public CatalogResponse findById(Long catalogId) {
        Catalog catalog = fetchCatalog(catalogId);

        return CatalogMapper.toDto(catalog);
    }

    @Override
    public CatalogOrderInfo getCatalogOrderInfo(Long catalogId) {
        return catalogRepository.getCatalogOrderInfo(catalogId)
                .orElseThrow(() -> new CatalogNotFoundException("Invalid catalog id."));
    }

    @Override
    @Transactional
    public void updateUnits(Long stockId, Integer units) {
        Catalog catalog = catalogRepository.findByStockId(stockId)
                .orElseThrow(() -> new CatalogNotFoundException("Catalog not found for stock id "
                        + stockId));

        boolean isUpdated = false;

        if (units != null && units > 0) {
            catalog.setUnits(units);
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new RuntimeException("At least 1 field is required");
        }

        catalogRepository.save(catalog);
        log.info("Catalog units updated: {}", units);
    }

    @Override
    public CatalogOrderResponse getCatalogOrderResponse(Long catalogId) {
        Catalog catalog = fetchCatalog(catalogId);

        return CatalogMapper.toCatalogOrderDto(catalog);
    }

    private Catalog fetchCatalog(Long catalogId) {
        return catalogRepository.findById(catalogId)
                .orElseThrow(() -> new CatalogNotFoundException("Invalid catalog id."));
    }
}
