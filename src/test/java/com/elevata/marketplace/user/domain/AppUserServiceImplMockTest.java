package com.elevata.marketplace.user.domain;

import com.elevata.marketplace.user.AddAppUser;
import com.elevata.marketplace.user.AppUserRole;
import com.elevata.marketplace.user.AppUserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ApplicationModuleTest
class AppUserServiceImplMockTest {

    @MockitoBean
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserService appUserService;

    @Test
    void shouldSaveAppUserSuccessfully() {
        AddAppUser request = new AddAppUser("test name 1",
                "test1@gmail.com",
                "9012345678",
                "Customer");

        appUserService.save(request);
        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository, times(1)).save(captor.capture());
    }

    @Test
    void shouldMapAndSaveAppUserCorrectly() {
        AddAppUser request = new AddAppUser("test name 1",
                "test1@gmail.com",
                "9012345678",
                "Customer");

        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);

        appUserService.save(request);

        verify(appUserRepository, times(1)).save(captor.capture());

        AppUser savedUser = captor.getValue();

        assertEquals("test name 1", savedUser.getFullName());
        assertEquals("test1@gmail.com", savedUser.getEmail());
        assertEquals("9012345678", savedUser.getPhoneNumber());
        assertEquals(AppUserRole.CUSTOMER, savedUser.getRole());
    }

    @Test
    void shouldThrowExceptionForInvalidRole() {
        AddAppUser request = new AddAppUser("test name 1",
                "test1@gmail.com",
                "9012345678",
                "superadmin");

        assertThrows(IllegalArgumentException.class, () -> appUserService.save(request));

        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);

        verify(appUserRepository, never()).save(captor.capture());
    }
}