package com.elevata.marketplace.user.domain;

import com.elevata.marketplace.user.AddAppUser;
import com.elevata.marketplace.user.AppUserRole;
import com.elevata.marketplace.user.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppUserServiceImpl implements AppUserService {
    private static final Logger log = LoggerFactory.getLogger(AppUserServiceImpl.class);
    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    @Transactional
    public void save(AddAppUser appUser) {
        AppUser u = new AppUser(appUser.fullName(),
                appUser.email(),
                appUser.phoneNumber(),
                AppUserRole.valueOf(appUser.role().toUpperCase()));

        appUserRepository.save(u);
        log.info("[From the save] AppUser is saved : {}", u);
    }

    @Override
    public boolean validateUser(String email) {
        return appUserRepository.existsByEmail(email);
    }

    @Override
    public Long getIdByEmail(String email) {
        return appUserRepository.getIdByEmail(email);
    }
}
