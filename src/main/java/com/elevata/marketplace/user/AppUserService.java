package com.elevata.marketplace.user;

import org.springframework.stereotype.Service;

@Service
public interface AppUserService {
    void save(AddAppUser appUser);

    boolean validateUser(String email);

    Long getIdByEmail(String email);

    AppUserDto findByEmail(String email);
}
