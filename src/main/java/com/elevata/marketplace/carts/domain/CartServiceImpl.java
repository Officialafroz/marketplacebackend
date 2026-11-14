package com.elevata.marketplace.carts.domain;

import com.elevata.marketplace.carts.CartOrderRequest;
import com.elevata.marketplace.carts.CartRequest;
import com.elevata.marketplace.carts.CartResponse;
import com.elevata.marketplace.carts.CartService;
import com.elevata.marketplace.catalogs.CatalogOrderInfo;
import com.elevata.marketplace.catalogs.CatalogOrderResponse;
import com.elevata.marketplace.catalogs.CatalogService;
import com.elevata.marketplace.security.CurrentUser;
import com.elevata.marketplace.stock.StockUnitsUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
    public CartResponse addToCart(CartRequest cartRequest) {
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

        int updatedQuantity = catalogOrderInfo.quantity() - cart.getQuantity();
        eventPublisher.publishEvent(new StockUnitsUpdate(catalogOrderInfo.stockId(),
                updatedQuantity));

        log.info("[From the cart service] Cart saved : {}", cartRequest);
        return makeDto(cart);
    }

    @Override
    @Transactional
    public CartResponse remove(Long cartId) {
        Cart cart = fetchCart(cartId);

        cart.setCartStatus(CartStatus.REMOVED);
        cartRepository.save(cart);

        //Update the stock
        CatalogOrderInfo catalogOrderInfo = catalogService.getCatalogOrderInfo(cart.getCatalogId());
        int updatedQuantity = cart.getQuantity() + catalogOrderInfo.quantity();
        eventPublisher.publishEvent(new StockUnitsUpdate(catalogOrderInfo.stockId(),
                updatedQuantity));

        log.info("[From the cart service] Cart removed");
        return makeDto(cart);
    }

    @Override
    @Transactional
    public String toOrder(Long cartId) {
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
            return "Order request sent.";
        }
        return "Cart status : " + cart.getCartStatus().name();
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
}
