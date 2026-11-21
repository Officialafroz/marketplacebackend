package com.elevata.marketplace.orders.domain;

import com.elevata.marketplace.address.AddressResponse;
import com.elevata.marketplace.address.AddressService;
import com.elevata.marketplace.carts.BulkCartOrderRequest;
import com.elevata.marketplace.carts.CartOrderRequest;
import com.elevata.marketplace.catalogs.CatalogOrderInfo;
import com.elevata.marketplace.catalogs.CatalogOrderResponse;
import com.elevata.marketplace.catalogs.CatalogService;
import com.elevata.marketplace.commons.exceptions.OutOfStockException;
import com.elevata.marketplace.orders.OrderResponse;
import com.elevata.marketplace.orders.OrderService;
import com.elevata.marketplace.security.CurrentUser;
import com.elevata.marketplace.stock.ReduceStock;
import com.elevata.marketplace.stock.AddStock;
import com.elevata.marketplace.stock.StockStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<OrderResponse> save(OrderRequest orderRequest) {
        Long customerId = currentUser.id();
        addressService.verifyAddressForCustomer(customerId, orderRequest.addressId());

        Order order = createOrder(orderRequest.catalogId(),
                orderRequest.addressId(),
                orderRequest.quantity(),
                orderRequest.paymentOption(),
                customerId);

        return ResponseEntity.status(HttpStatus.CREATED).body(makeDto(order));
    }

    /// Canceled ordered cannot be used again
    @Override
    @Transactional
    public ResponseEntity<OrderResponse> cancel(Long orderId) {
        Long customerId = currentUser.id();
        Order order = orderRepository.findByCustomerIdAndOrderId(customerId, orderId)
                .orElseThrow(() -> new OrderNotFoundException("Invalid order id "
                        + orderId));
        OrderResponse orderResponse;

        if (order.getOrderStatus().equals(OrderStatus.CANCELED)) {
            orderResponse = makeDto(order);
            return ResponseEntity.ok(orderResponse);
        }
        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepository.save(order);

        // Updates the stock
        CatalogOrderInfo catalogOrderInfo = catalogService
                .getCatalogOrderInfo(order.getCatalogId());
        eventPublisher.publishEvent(new AddStock(catalogOrderInfo.stockId(),
                order.getQuantity(),
                catalogOrderInfo.sellerId()));

        /*
            Add handling of payment when the order is canceled
         */
        log.info("[Order canceled] From the order service");
        orderResponse = makeDto(order);
        return ResponseEntity.ok(orderResponse);
    }

    @Override
    public void cartToOrder(CartOrderRequest cartOrderRequest) {
        Long customerId = currentUser.id();

        //Address must not be default, option to choose should be present.
        Long addressId = addressService.getDefaultAddressByCustomerId(customerId);
        Order order = createOrder(cartOrderRequest.catalogOrderResponse().catalogId(),
                addressId,
                cartOrderRequest.quantity(),
                PaymentOption.CASH_ON_DELIVERY.name(),
                customerId);

        log.info("[From the order service] cart order created info: {}", order.getOrderId());
    }

    @Override
    @Transactional
    public void allCartsToOrder(BulkCartOrderRequest bulkCartOrderRequest) {
        if (bulkCartOrderRequest.orderRequest().isEmpty()) {
            return;
        }

        bulkCartOrderRequest.orderRequest().forEach(this::cartToOrder);
    }

    @Override
    public ResponseEntity<List<OrderResponse>> findAllActive() {
        Long customerId = currentUser.id();
        List<Order> orders = orderRepository
                .findAllByCustomerIdAndOrderStatus(customerId, OrderStatus.PLACED);
        List<OrderResponse> orderResponses = new ArrayList<>(orders.size());

        orders.forEach(order -> orderResponses.add(makeDto(order)));

        return ResponseEntity.status(HttpStatus.OK).body(orderResponses);
    }

    private OrderResponse makeDto(Order order) {
        AddressResponse addressResponse = addressService.findById(order.getAddressId());
        CatalogOrderResponse catalogOrderResponse = catalogService
                .getCatalogOrderResponse(order.getCatalogId());
        return OrderMapper.toDto(order, addressResponse, catalogOrderResponse);
    }

    private Order createOrder(Long catalogId, Long addressId, int quantity,
                              String paymentOption, Long customerId
    ) {
        CatalogOrderInfo catalogOrderInfo =
                catalogService.getCatalogOrderInfo(catalogId);
        StockStatus stockStatus = catalogOrderInfo.stockStatus();

        // Validates stock availability
        if (stockStatus == StockStatus.OUT_OF_STOCK
            || stockStatus == StockStatus.EXPIRED
        ) {
            throw new OutOfStockException("Cannot create order. Stock unavailable.");
        }

        if (quantity <= 0 || quantity > catalogOrderInfo.quantity()) {
            throw new InvalidQuantityException("Invalid quantity value.");
        }

        // Validates address ownership
        addressService.verifyAddressForCustomer(customerId, addressId);

        // Calculates total price
        BigDecimal totalPrice = catalogOrderInfo.price()
                .multiply(BigDecimal.valueOf(quantity));

        OrderRequest orderRequest = new OrderRequest(catalogId, addressId,
                quantity, paymentOption);

        Order order = OrderMapper.toEntity(orderRequest, addressId,
                customerId, totalPrice, OrderStatus.PLACED);

        order = orderRepository.save(order);

        eventPublisher.publishEvent(
                new ReduceStock(catalogOrderInfo.stockId(), quantity,
                        catalogOrderInfo.sellerId()));

        log.info("Order created: {}", order.getOrderId());

        return order;
    }
}
