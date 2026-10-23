package com.elevata.marketplace.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    boolean existsByEmail(String email);

    @Query("SELECT u.appUserId FROM AppUser u WHERE u.email = :email")
    Long getIdByEmail(@Param("email") String email);
}
