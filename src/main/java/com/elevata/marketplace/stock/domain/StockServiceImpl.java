package com.elevata.marketplace.stock.domain;

import com.elevata.marketplace.security.CurrentUser;
import com.elevata.marketplace.stock.StockResponse;
import com.elevata.marketplace.stock.StockService;
import com.elevata.marketplace.stock.StockStatus;
import com.elevata.marketplace.stock.CatalogUpdate;
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
    private final ApplicationEventPublisher eventPublisher;
    private final CurrentUser currentUser;

    @Autowired
    public StockServiceImpl(StockRepository stockRepository, ApplicationEventPublisher eventPublisher, CurrentUser currentUser) {
        this.stockRepository = stockRepository;
        this.eventPublisher = eventPublisher;
        this.currentUser = currentUser;
    }

    @Override
    @Transactional
    public ResponseEntity<StockResponse> add(AddStockForProduct stock) {
        Long sellerId = currentUser.id();
        Stock s = StockMapper.toEntity(sellerId, stock);
        s = stockRepository.save(s);
        StockResponse stockResponse = StockMapper.convertToDto(s);

        eventPublisher.publishEvent(stockResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(stockResponse);
    }

    @Override
    public ResponseEntity<Page<StockResponse>> getProductStockForCustomer(int pageNumber) {
        Long sellerId = currentUser.id();
        Page<StockResponse> stockResponsePage = stockRepository
                .getProductStockForSeller(sellerId, PageRequest.of(pageNumber, PRODUCT_PAGE_SIZE));
        return ResponseEntity.ok(stockResponsePage);
    }

    @Override
    public ResponseEntity<StockResponse> addStock(Long stockId, Integer units) {
        Long sellerId = currentUser.id();
        StockResponse stockResponse = addStock(stockId, units, sellerId);
        return ResponseEntity.ok(stockResponse);
    }

    @Override
    @Transactional
    public StockResponse addStock(Long stockId, Integer units, Long sellerId) {
        if (units == null || units <= 0) {
            throw new StockException("Units must be greater than zero.");
        }

        return updateStock(stockId, units, sellerId, true);
    }

    @Override
    @Transactional
    public void reduceStock(Long stockId, Integer units, Long sellerId) {
        if (units == null || units <= 0) {
            throw new StockException("Units must be greater than zero.");
        }

        updateStock(stockId, units, sellerId, false);
    }

    @Override
    public Page<StockResponse> getAvailableStock(int pageNumber, int catalogPageSize) {
        Long sellerId = currentUser.id();
        return stockRepository.getAvailableStockForSeller(sellerId, PageRequest.of(pageNumber, catalogPageSize));
    }

    private StockResponse updateStock(Long stockId, Integer units, Long sellerId,
                                      boolean increase
    ) {

        Stock stock = stockRepository
                .findBySellerIdAndStockId(sellerId, stockId)
                .orElseThrow(() -> new StockNotFoundException("Stock not found"));

        int currentUnits = stock.getUnits();
        int updatedUnits;

        if (increase) {
            updatedUnits = currentUnits + units;
        } else {
            if (units > currentUnits) {
                throw new StockException("Insufficient stock to reduce.");
            }

            updatedUnits = currentUnits - units;
        }

        boolean isStatusUpdated = false;

        if (updatedUnits == 0) {
            stock.setStockStatus(StockStatus.OUT_OF_STOCK);
            isStatusUpdated = true;
        } else if (currentUnits == 0 && updatedUnits > 0) {
            stock.setStockStatus(StockStatus.AVAILABLE);
            isStatusUpdated = true;
        }

        stock.setUnits(updatedUnits);

        stockRepository.save(stock);

        eventPublisher.publishEvent(new CatalogUpdate(sellerId, stockId,
                updatedUnits, isStatusUpdated, stock.getStockStatus()));

        log.info("Stock units updated: {}", updatedUnits);

        return StockMapper.convertToDto(stock);
    }
}
