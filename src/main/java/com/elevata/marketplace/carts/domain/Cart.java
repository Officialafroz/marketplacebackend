package com.elevata.marketplace.carts.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@DynamicInsert
class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;
    private Long catalogId;
    private int quantity;
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private CartStatus cartStatus;
    private Long customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Cart() {
    }

    public Cart(Long catalogId, int quantity, BigDecimal totalPrice, CartStatus cartStatus, Long customerId) {
        this.catalogId = catalogId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.cartStatus = cartStatus;
        this.customerId = customerId;
    }

    public Long getCartId() {
        return cartId;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public CartStatus getCartStatus() {
        return cartStatus;
    }

    public void setCartStatus(CartStatus cartStatus) {
        this.cartStatus = cartStatus;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
