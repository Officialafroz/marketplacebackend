package com.elevata.marketplace.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    Otp findByAppUserId(Long appUserId);

    @Query("SELECT o FROM Otp o ORDER BY o.createdAt DESC LIMIT 1")
    Otp findByAppUserIdOrderByCreatedAtDesc(Long appUserId);
}
