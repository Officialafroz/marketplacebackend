package com.elevata.marketplace.stock.domain;

import com.elevata.marketplace.stock.StockResponse;
import com.elevata.marketplace.stock.StockService;
import com.elevata.marketplace.stock.StockStatus;
import com.elevata.marketplace.stock.StockUpdate;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class StockServiceImpl implements StockService {
    private static final Logger log = LoggerFactory.getLogger(StockServiceImpl.class);
    private static final int PRODUCT_PAGE_SIZE = 30;
    private final StockRepository stockRepository;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public StockServiceImpl(StockRepository stockRepository, ApplicationEventPublisher eventPublisher) {
        this.stockRepository = stockRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ResponseEntity<StockResponse> add(AddStockForProduct stock) {
        Stock s = StockMapper.toEntity(stock);
        s = stockRepository.save(s);
        StockResponse stockResponse = StockMapper.convertToDto(s);

        eventPublisher.publishEvent(stockResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(stockResponse);
    }

    @Override
    public ResponseEntity<Page<StockResponse>> getProductStock(int pageNumber) {
        Page<StockResponse> stockResponsePage = stockRepository
                .getProductStock(PageRequest.of(pageNumber, PRODUCT_PAGE_SIZE));
        return ResponseEntity.ok(stockResponsePage);
    }

    @Override
    @Transactional
    public ResponseEntity<StockResponse> updateUnitsOrStatus(Long stockId,
                                                             @Nullable Integer units,
                                                             @Nullable String status
    ) {
        Stock s = fetchStock(stockId);
        boolean isUpdated = false;
        StockStatus stockStatus = null;

        if (units != null && units > 0) {
            int updatedUnits = s.getUnits() + units;
            s.setUnits(updatedUnits);
            isUpdated = true;
        }

        //Analyze
        if ((StockStatus.OUT_OF_STOCK.name().equalsIgnoreCase(status)
                || StockStatus.AVAILABLE.name().equalsIgnoreCase(status)
                || StockStatus.EXPIRED.name().equalsIgnoreCase(status))
        ) {
            stockStatus = StockStatus.valueOf(status.toUpperCase());
            s.setStockStatus(stockStatus);
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new RuntimeException("At least 1 field is required");
        }

        log.info("Stock status: {}", status);

        eventPublisher.publishEvent(new StockUpdate(stockId, units, stockStatus));
        s = stockRepository.save(s);

        StockResponse stockResponse = StockMapper.convertToDto(s);
        return ResponseEntity.ok(stockResponse);
    }

    @Override
    @Transactional
    public void updateUnits(Long stockId, Integer units) {
        Stock s = fetchStock(stockId);
        boolean isUpdated = false;

        if (units != null && units > 0) {
            s.setUnits(units);
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new RuntimeException("At least 1 field is required");
        }

        log.info("Stock units updated: {}", units);
        stockRepository.save(s);
    }

    @Override
    public Page<StockResponse> getAvailableStock(int pageNumber, int catalogPageSize) {
        return stockRepository.getAvailableStock(PageRequest.of(pageNumber, catalogPageSize));
    }

    private Stock fetchStock(Long stockId) {
        return stockRepository.findById(stockId)
                .orElseThrow(() -> new StockNotFoundException(
                        "Stock not found for id " + stockId));
    }
}
