package com.elevata.marketplace.auth.domain;

import com.elevata.marketplace.auth.AddAppUser;
import com.elevata.marketplace.auth.AuthService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {
    private ApplicationEventPublisher eventPublisher;

    public AuthServiceImpl(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public UserResponse registerCustomer(AddAppUser appUser) {
        eventPublisher.publishEvent(appUser);

        return null;
    }
}
