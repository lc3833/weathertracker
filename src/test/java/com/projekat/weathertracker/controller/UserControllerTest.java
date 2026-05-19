package com.projekat.weathertracker.controller;

import com.projekat.weathertracker.model.User;
import com.projekat.weathertracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(10L);
        testUser.setUsername("lazar_cvetkovic");
        testUser.setPassword("lozinka123");
        testUser.setEmail("lazar@test.com");
    }

    @Test
    void testRegister_Success() {
        when(userService.createUser(any(User.class))).thenReturn(testUser);
        ResponseEntity<?> response = userController.register(testUser);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }

    @Test
    void testRegister_Failure() {
        when(userService.createUser(any(User.class))).thenThrow(new RuntimeException("Greška pri registraciji"));
        ResponseEntity<?> response = userController.register(testUser);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Greška pri registraciji", response.getBody());
    }

    @Test
    void testLogin_Success() {
        when(userService.login("lazar_cvetkovic", "lozinka123")).thenReturn(testUser);
        ResponseEntity<?> response = userController.login(testUser);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }

    @Test
    void testLogin_Failure() {
        when(userService.login("lazar_cvetkovic", "lozinka123")).thenThrow(new RuntimeException("Pogrešni podaci"));
        ResponseEntity<?> response = userController.login(testUser);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Pogrešni podaci", response.getBody());
    }
}