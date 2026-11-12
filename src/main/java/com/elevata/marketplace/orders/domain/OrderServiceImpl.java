package com.elevata.marketplace.orders.domain;

import com.elevata.marketplace.address.AddressResponse;
import com.elevata.marketplace.address.AddressService;
import com.elevata.marketplace.catalogs.CatalogOrderInfo;
import com.elevata.marketplace.catalogs.CatalogOrderResponse;
import com.elevata.marketplace.catalogs.CatalogResponse;
import com.elevata.marketplace.catalogs.CatalogService;
import com.elevata.marketplace.orders.OrderResponse;
import com.elevata.marketplace.orders.OrderService;
import com.elevata.marketplace.security.CurrentUser;
import com.elevata.marketplace.stock.StockUnitsUpdate;
import com.elevata.marketplace.stock.StockUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final CurrentUser currentUser;
    private final OrderRepository orderRepository;
    private final AddressService addressService;
    private final CatalogService catalogService;
    private final ApplicationEventPublisher eventPublisher;

    public OrderServiceImpl(CurrentUser currentUser, OrderRepository orderRepository, AddressService addressService, CatalogService catalogService, ApplicationEventPublisher eventPublisher) {
        this.currentUser = currentUser;
        this.orderRepository = orderRepository;
        this.addressService = addressService;
        this.catalogService = catalogService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public OrderResponse save(OrderRequest orderRequest) {
        Long id = currentUser.id();
        CatalogOrderInfo catalogOrderInfo = catalogService.getCatalogOrderInfo(orderRequest.catalogId());
        //Make sure we have the stock
        if (orderRequest.quantity() > catalogOrderInfo.quantity()) {
            throw new OrderQuantityLimitReachedException("Quantity is above the stock!");
        }

        //Calculate the total price as per the quantity
        BigDecimal totalPrice = catalogOrderInfo.price().multiply(BigDecimal
                .valueOf(orderRequest.quantity()));

        Order order = OrderMapper.toEntity(orderRequest, id, totalPrice, OrderStatus.PLACED);
        order = orderRepository.save(order);

        //Update the stock
        int updatedQuantity = catalogOrderInfo.quantity() - orderRequest.quantity();
        eventPublisher.publishEvent(new StockUnitsUpdate(catalogOrderInfo.stockId(),
                updatedQuantity));

        log.info("[From the order service] order created info: {}", orderRequest);
        return makeDto(order);
    }

    //Canceled ordered cannot be used again
    @Override
    @Transactional
    public OrderResponse cancel(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Invalid order id " + orderId));

        if (order.getOrderStatus().equals(OrderStatus.CANCELED)) {
            return makeDto(order);
        }
        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepository.save(order);

        // Update the stock
        CatalogOrderInfo catalogOrderInfo = catalogService.getCatalogOrderInfo(order.getCatalogId());
        int updatedQuantity = order.getQuantity() + catalogOrderInfo.quantity();
        eventPublisher.publishEvent(new StockUnitsUpdate(catalogOrderInfo.stockId(), updatedQuantity));

        /*
            Add handling of payment when the order is canceled
         */
        log.info("[Order canceled] From the order service");
        return makeDto(order);
    }

    private OrderResponse makeDto(Order order) {
        AddressResponse addressResponse = addressService.findById(order.getAddressId());
        CatalogOrderResponse catalogOrderResponse = catalogService
                .getCatalogOrderResponse(order.getCatalogId());
        return OrderMapper.toDto(order, addressResponse, catalogOrderResponse);
    }
}
