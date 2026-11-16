package com.elevata.marketplace.carts.domain;

import com.elevata.marketplace.carts.*;
import com.elevata.marketplace.catalogs.CatalogOrderInfo;
import com.elevata.marketplace.catalogs.CatalogOrderResponse;
import com.elevata.marketplace.catalogs.CatalogService;
import com.elevata.marketplace.security.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);
    private final CartRepository cartRepository;
    private final CatalogService catalogService;
    private final CurrentUser currentUser;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, CatalogService catalogService, CurrentUser currentUser, ApplicationEventPublisher eventPublisher) {
        this.cartRepository = cartRepository;
        this.catalogService = catalogService;
        this.currentUser = currentUser;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ResponseEntity<CartResponse> addToCart(CartRequest cartRequest) {
        Long id = currentUser.id();
        CatalogOrderInfo catalogOrderInfo = catalogService
                .getCatalogOrderInfo(cartRequest.catalogId());
        //Make sure we have the stock
        if (cartRequest.quantity() > catalogOrderInfo.quantity()) {
            throw new QuantityLimitReachedException("Quantity is above the stock!");
        }

        //Calculate the total price as per the quantity
        BigDecimal totalPrice = catalogOrderInfo.price().multiply(BigDecimal
                .valueOf(cartRequest.quantity()));

        Cart cart = CartMapper.toEntity(cartRequest, totalPrice, id, CartStatus.CART);
        cart = cartRepository.save(cart);

        log.info("[From the cart service] Cart saved : {}", cartRequest);
        CartResponse cartResponse = makeDto(cart);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartResponse);
    }

    @Override
    @Transactional
    public ResponseEntity<CartResponse> remove(Long cartId) {
        Cart cart = fetchCart(cartId);

        cart.setCartStatus(CartStatus.REMOVED);
        cartRepository.save(cart);

        log.info("[From the cart service] Cart removed");
        CartResponse cartResponse = makeDto(cart);
        return ResponseEntity.ok(cartResponse);
    }

    @Override
    @Transactional
    public ResponseEntity<String> toOrder(Long cartId) {
        Cart cart = fetchCart(cartId);
        if (cart.getCartStatus().equals(CartStatus.CART)) {
            CatalogOrderResponse catalogOrderResponse = catalogService
                    .getCatalogOrderResponse(cart.getCatalogId());

            CartOrderRequest orderRequest = CartMapper.toCartOrderDto(cart,
                    catalogOrderResponse);
            eventPublisher.publishEvent(orderRequest);

            cart.setCartStatus(CartStatus.ORDERED);
            cart = cartRepository.save(cart);

            log.info("[Cart order request sent] Order request : {}", orderRequest);
            return ResponseEntity.ok("Order request sent.");
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body("Can't order it.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> allCartsToOrder() {
        List<Cart> carts = fetchAllCarts();
        if (carts.isEmpty()) {
            return ResponseEntity.ok("Customer doesn't have a cart.");
        }

        List<CartOrderRequest> orderRequestList = new ArrayList<>(carts.size());

        carts.forEach(cart -> {
            CatalogOrderResponse catalogOrderResponse = catalogService
                    .getCatalogOrderResponse(cart.getCatalogId());
            CartOrderRequest orderRequest = CartMapper.toCartOrderDto(cart, catalogOrderResponse);
            orderRequestList.add(orderRequest);
        });

        eventPublisher.publishEvent(new BulkCartOrderRequest(orderRequestList));

        carts.forEach(cart -> {
            cart.setCartStatus(CartStatus.ORDERED);
        });

        cartRepository.saveAll(carts);

        log.info("[All carts order request sent] From the Cart service");
        return ResponseEntity.ok("Bulk order request sent.");
    }

    @Override
    public ResponseEntity<List<CartResponse>> findAllActive() {
        List<Cart> carts = fetchAllCarts();

        List<CartResponse> cartResponseList = new ArrayList<>(carts.size());
        carts.forEach(cart -> {
            CatalogOrderResponse catalogOrderResponse = catalogService
                    .getCatalogOrderResponse(cart.getCatalogId());
            CartResponse cartResponse = new CartResponse(catalogOrderResponse,
                    cart.getQuantity(),
                    cart.getTotalPrice(),
                    cart.getCartStatus(),
                    cart.getCustomerId());

            cartResponseList.add(cartResponse);
        });

        log.info("[Cart response list created] From the Carts service");
        return ResponseEntity.ok(cartResponseList);
    }

    private CartResponse makeDto(Cart cart) {
        CatalogOrderResponse catalogOrderResponse = catalogService
                .getCatalogOrderResponse(cart.getCatalogId());
        return CartMapper.toDto(cart, catalogOrderResponse);
    }

    private Cart fetchCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Invalid cart id " + cartId));
    }

    private List<Cart> fetchAllCarts() {
        Long id = currentUser.id();

        return cartRepository.findAllActiveByCustomerId(id)
                .orElseThrow(() -> new CartNotFoundException("Carts not found for the customer."));
    }
}
