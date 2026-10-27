package com.elevata.marketplace.address.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT COUNT(*) FROM Address a WHERE a.appUserId = :appUserId")
    int countByAppUserId(@Param("appUserId") Long appUserId);

    Optional<Address> findByAddressIdAndAppUserId(Long addressId, Long appUserId);
}
