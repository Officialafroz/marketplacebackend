package com.elevata.marketplace.carts.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.customerId = :customerId AND c.cartStatus = 'CART'")
    Optional<List<Cart>> findAllActiveByCustomerId(@Param("customerId") Long customerId);
}
