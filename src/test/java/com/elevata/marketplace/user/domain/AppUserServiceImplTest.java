package com.elevata.marketplace.user.domain;

import com.elevata.marketplace.user.AppUserDto;
import com.elevata.marketplace.user.AppUserRole;
import com.elevata.marketplace.user.AppUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;

import static org.junit.jupiter.api.Assertions.*;

@ApplicationModuleTest
class AppUserServiceImplTest {

    @Autowired
    private AppUserService appUserService;

    @Test
    void validateUserByEmailShouldReturnTrueOnSuccess() {
        //Email validation is at the DTO layer
        assertFalse(appUserService.validateUser("hello@gmail.com"));
        assertFalse(appUserService.validateUser("-hello@gmail.com"));
        assertTrue(appUserService.validateUser("test@gmail.com"));
        assertThrows(IllegalArgumentException.class, () -> appUserService.validateUser(null));
    }

    @Test
    void validateByIdShouldReturnTrueOnSuccess() {
        assertTrue(appUserService.validateById(4L));
        assertFalse(appUserService.validateById(101L));
        assertThrows(IllegalArgumentException.class, () -> appUserService.validateById(-13L));
        assertThrows(IllegalArgumentException.class, () -> appUserService.validateById(0L));
        assertThrows(IllegalArgumentException.class, () -> appUserService.validateById(null));
    }

    @Test
    void testGetIdByEmailShouldReturnUserId() {
        assertEquals(1, appUserService.getIdByEmail("test@gmail.com"));
        assertNotEquals(2, appUserService.getIdByEmail("test@gmail.com"));
        assertThrows(IllegalArgumentException.class, () -> appUserService.getIdByEmail(null));
    }

    @Test
    void testFindByEmailShouldReturnAppUserDto() {
        AppUserDto dto = new AppUserDto(1L, AppUserRole.CUSTOMER);

        assertEquals(dto, appUserService.findByEmail("test@gmail.com"));
        assertNotEquals(dto, appUserService.findByEmail("rebuildingnewself@gmail.com"));
        assertThrows(IllegalArgumentException.class, () -> appUserService.findByEmail(null));
//        assertThrows(EmptyResultDataAccessException.class, () -> appUserService.findByEmail("test"));
    }

    @Test
    void testFindByIdShouldReturnAppUserDto() {
        AppUserDto dto = new AppUserDto(1L, AppUserRole.CUSTOMER);

        assertEquals(dto, appUserService.findById(1));
        assertNotEquals(dto, appUserService.findById(6));
        assertThrows(IllegalArgumentException.class, () -> appUserService.findById(-13));
        assertThrows(IllegalArgumentException.class, () -> appUserService.findById(0));
    }
}