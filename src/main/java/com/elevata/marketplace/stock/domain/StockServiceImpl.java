package com.elevata.marketplace.stock.domain;

import com.elevata.marketplace.stock.StockResponse;
import com.elevata.marketplace.stock.StockService;
import com.elevata.marketplace.stock.StockStatus;
import com.elevata.marketplace.stock.StockUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public StockResponse add(AddStockForProduct stock) {
        Stock s = StockMapper.toEntity(stock);
        s = stockRepository.save(s);
        StockResponse stockResponse = StockMapper.convertToDto(s);

        eventPublisher.publishEvent(stockResponse);

        return stockResponse;
    }

    @Override
    public Page<StockResponse> getProductStock(int pageNumber) {
        return stockRepository.getProductStock(PageRequest.of(pageNumber,
                PRODUCT_PAGE_SIZE));
    }

    @Override
    @Transactional
    public StockResponse updateUnitsOrStatus(Long stockId, Integer units, String status) {
        Stock s = stockRepository.findById(stockId)
                .orElseThrow(() -> new StockNotFoundException(
                        "Stock not found for id " + stockId));
        byte count = 0;
        StockStatus stockStatus = null;

        if (units != null && units > 0) {
            s.setUnits(units);
            count++;
        }

        //Analyze
        if ((StockStatus.OUT_OF_STOCK.name().equalsIgnoreCase(status)
                || StockStatus.AVAILABLE.name().equalsIgnoreCase(status)
                || StockStatus.EXPIRED.name().equalsIgnoreCase(status))
        ) {
            stockStatus = StockStatus.valueOf(status.toUpperCase());
            s.setStockStatus(stockStatus);
            count++;
        }

        if (count == 0) {
            throw new RuntimeException("At least 1 field is required");
        }

        log.info("Stock status: {}", status);

        eventPublisher.publishEvent(new StockUpdate(stockId, units, stockStatus));
        s = stockRepository.save(s);

        return StockMapper.convertToDto(s);
    }

    @Override
    public Page<StockResponse> getAvailableStock(int pageNumber, int catalogPageSize) {
        return stockRepository.getAvailableStock(PageRequest.of(pageNumber, catalogPageSize));
    }
}
