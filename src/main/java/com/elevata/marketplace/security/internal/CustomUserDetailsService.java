package com.elevata.marketplace.security.internal;

import com.elevata.marketplace.user.AppUserDto;
import com.elevata.marketplace.user.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AppUserService appUserService;

    @Autowired
    public CustomUserDetailsService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUserDto appUserDto = appUserService.findById(Long.parseLong(username));
        return new CustomUserDetails(appUserDto.appUserId(), appUserDto.role().name());
    }
}
