package com.elevata.marketplace.auth;

import com.elevata.marketplace.user.AddAppUser;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    String registerCustomer(AddAppUser appUser);
}
