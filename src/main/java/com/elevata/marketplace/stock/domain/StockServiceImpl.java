package com.elevata.marketplace.stock.domain;

import com.elevata.marketplace.stock.StockResponse;
import com.elevata.marketplace.stock.StockService;
import com.elevata.marketplace.stock.StockStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockServiceImpl implements StockService {
    private static final Logger log = LoggerFactory.getLogger(StockServiceImpl.class);
    private static final int PRODUCT_PAGE_SIZE = 30;
    private final StockRepository stockRepository;

    @Autowired
    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    @Transactional
    public StockResponse add(AddStockForProduct stock) {
        Stock s = new Stock(stock.units(),
                stock.price(),
                stock.manufacturingDate(),
                stock.expiryDate(),
                stock.productId());

        s = stockRepository.save(s);

        return new StockResponse(s.getStockId(),
                s.getProductId(),
                s.getUnits(),
                s.getPrice(),
                s.getManufacturingDate(),
                s.getExpiryDate(),
                s.getStockStatus() == null ? StockStatus.OUT_OF_STOCK : s.getStockStatus(),
                s.getCreatedAt(),
                s.getUpdatedAt());
    }

    @Override
    public Page<StockResponse> getProductStock(int pageNumber) {
        return stockRepository.getProductStock(PageRequest.of(pageNumber,
                PRODUCT_PAGE_SIZE));
    }

    @Override
    @Transactional
    public StockResponse updatePriceOrStatus(Long stockId, Integer units, String status) {
        Stock s = stockRepository.findById(stockId)
                .orElseThrow(() -> new StockNotFoundException(
                        "Stock not found for id " + stockId));

        if (units != null) {
            s.setUnits(units);
        }

        //Analyze
        if ((StockStatus.OUT_OF_STOCK.name().equalsIgnoreCase(status)
                || StockStatus.AVAILABLE.name().equalsIgnoreCase(status)
                || StockStatus.EXPIRED.name().equalsIgnoreCase(status))
        ) {
            s.setStockStatus(StockStatus.valueOf(status.toUpperCase()));
        }

        log.info("--------------------------------- Stock status: {}", status);

        s = stockRepository.save(s);

        return new StockResponse(s.getStockId(),
                s.getProductId(),
                s.getUnits(),
                s.getPrice(),
                s.getManufacturingDate(),
                s.getExpiryDate(),
                s.getStockStatus(),
                s.getCreatedAt(),
                s.getUpdatedAt());
    }

    @Override
    public Page<StockResponse> getAvailableStock(int pageNumber, int catalogPageSize) {
        return stockRepository.getAvailableStock(PageRequest.of(pageNumber, catalogPageSize));
    }
}
