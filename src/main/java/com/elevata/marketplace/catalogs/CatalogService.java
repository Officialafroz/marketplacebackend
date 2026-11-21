package com.elevata.marketplace.catalogs;

import com.elevata.marketplace.stock.StockResponse;
import com.elevata.marketplace.stock.CatalogUpdate;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CatalogService {
    ResponseEntity<Page<CatalogResponse>> getCatalogs(int pageNumber);

    void create(StockResponse stockResponse);

    ResponseEntity<Page<CatalogResponse>> search(String productName, int pageNumber);

    CatalogResponse findById(Long catalogId);

    CatalogOrderInfo getCatalogOrderInfo(Long catalogId);

    void updateStock(CatalogUpdate catalogUpdate);

    CatalogOrderResponse getCatalogOrderResponse(Long catalogId);
}
