package com.elevata.marketplace.auth;

import com.elevata.marketplace.auth.domain.UserResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    UserResponse registerCustomer(AddAppUser appUser);
}
