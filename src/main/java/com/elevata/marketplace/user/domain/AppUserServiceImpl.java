package com.elevata.marketplace.user.domain;

import com.elevata.marketplace.user.AddAppUser;
import com.elevata.marketplace.user.AppUserDto;
import com.elevata.marketplace.user.AppUserRole;
import com.elevata.marketplace.user.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class AppUserServiceImpl implements AppUserService {
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
        if (isNotNull(email)) return appUserRepository.existsByEmail(email);
        throw new IllegalArgumentException("Email must not be null.");
    }

    @Override
    public boolean validateById(Long id) {
        if (id != null && id > 0) return appUserRepository.existsById(id);
        throw new IllegalArgumentException("Id must be greater than 0.");
    }

    @Override
    public Long getIdByEmail(String email) {
        if (isNotNull(email)) return appUserRepository.getIdByEmail(email);
        throw new IllegalArgumentException("Email must not be null.");
    }

    @Override
    public AppUserDto findByEmail(String email) {
        if (isNotNull(email)) return appUserRepository.findByEmail(email);
        throw new IllegalArgumentException("Email must not be null.");
    }

    @Override
    public AppUserDto findById(long id) {
        if (id > 0) return appUserRepository.findByAppUserId(id);
        throw new IllegalArgumentException("Id must be greater than 0.");
    }

    private boolean isNotNull(String value) {
        return value != null;
    }
}
