package com.projekat.weathertracker.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    @Test
    void testUserGettersAndSetters() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test_user");
        user.setEmail("test@test.com");
        user.setPassword("pass123");
        user.setPhoneNumber("060123456");

        assertEquals(1L, user.getId());
        assertEquals("test_user", user.getUsername());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("pass123", user.getPassword());
        assertEquals("060123456", user.getPhoneNumber());
    }
}