package com.elevata.marketplace.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface OtpRepository extends JpaRepository<Otp, Long> {
    Otp findByAppUserId(Long appUserId);

    @Query("""
        SELECT o
        FROM Otp o
        WHERE o.appUserId = :appUserId
        ORDER BY o.createdAt DESC LIMIT 1
    """)
    Optional<Otp> findByAppUserIdOrderByCreatedAtDesc(@Param("appUserId") Long appUserId);
}
