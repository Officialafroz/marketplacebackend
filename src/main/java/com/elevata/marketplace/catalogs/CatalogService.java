package com.elevata.marketplace.catalogs;

import com.elevata.marketplace.stock.StockResponse;
import com.elevata.marketplace.stock.StockUpdate;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface CatalogService {
    Page<CatalogResponse> getCatalogs(int pageNumber);

    void create(StockResponse stockResponse);

    void updateUnitsOrStatus(StockUpdate stockUpdate);

    Page<CatalogResponse> search(String productName, int pageNumber);

    CatalogResponse findById(Long catalogId);

    CatalogOrderInfo getCatalogOrderInfo(Long catalogId);

    void updateUnits(Long stockId, Integer units);

    CatalogOrderResponse getCatalogOrderResponse(Long catalogId);
}
