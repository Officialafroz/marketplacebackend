package com.elevata.marketplace.user.eventhandlers;

import com.elevata.marketplace.auth.AddAppUser;
import com.elevata.marketplace.user.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventHandler {
    private static final Logger log = LoggerFactory.getLogger(UserEventHandler.class);
    private final AppUserService appUserService;

    UserEventHandler(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @ApplicationModuleListener
    void save(AddAppUser appUser) {
        log.info("[From the user event handler] event called: {}", appUser);
        appUserService.save(appUser);
    }

}
