package com.elevata.marketplace.catalogs.domain;

import com.elevata.marketplace.catalogs.*;
import com.elevata.marketplace.product.ProductResponse;
import com.elevata.marketplace.product.ProductService;
import com.elevata.marketplace.stock.StockResponse;
import com.elevata.marketplace.stock.CatalogUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class CatalogServiceImpl implements CatalogService {
    private static final int CATALOG_PAGE_SIZE = 50;
    private static final Logger log = LoggerFactory.getLogger(CatalogServiceImpl.class);
    private final ProductService productService;
    private final CatalogRepository catalogRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public CatalogServiceImpl(ProductService productService, CatalogRepository catalogRepository, ApplicationEventPublisher eventPublisher) {
        this.productService = productService;
        this.catalogRepository = catalogRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public ResponseEntity<Page<CatalogResponse>> getCatalogs(int pageNumber) {
        Page<CatalogResponse> catalogResponsePage = catalogRepository
                .findAllResponses(PageRequest.of(pageNumber, CATALOG_PAGE_SIZE));
        return ResponseEntity.ok(catalogResponsePage);
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
    public ResponseEntity<Page<CatalogResponse>> search(String productName, int pageNumber) {
        Page<CatalogResponse> catalogResponsePage = catalogRepository
                .search(productName, PageRequest.of(pageNumber, CATALOG_PAGE_SIZE));
        return ResponseEntity.ok(catalogResponsePage);
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
    public void updateStock(CatalogUpdate catalogUpdate) {
        Catalog catalog = catalogRepository.findBySellerIdAndStockId(
                catalogUpdate.sellerId(), catalogUpdate.stockId())
                .orElseThrow(() -> new CatalogNotFoundException("Catalog not found"));

        catalog.setUnits(catalogUpdate.units());

        if (catalogUpdate.isStatusUpdated() && catalogUpdate.stockStatus() != null) {
            catalog.setStockStatus(catalogUpdate.stockStatus());
        }

        catalog = catalogRepository.save(catalog);
        eventPublisher.publishEvent(new VerifyCartStock(catalog.getCatalogId(),
                catalog.getUnits()));

        log.info("Catalog units updated: {}", catalog.getUnits());
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
