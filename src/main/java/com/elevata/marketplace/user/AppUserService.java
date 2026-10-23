package com.elevata.marketplace.user;

import com.elevata.marketplace.auth.AddAppUser;
import org.springframework.stereotype.Service;

@Service
public interface AppUserService {
    void save(AddAppUser appUser);
}
