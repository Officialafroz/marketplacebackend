package com.elevata.marketplace.carts.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CartRepository extends JpaRepository<Cart, Long> {
}
