package com.projekat.weathertracker.controller;

import com.projekat.weathertracker.dto.UserDTO;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(10L);
        testUser.setUsername("lazar_cvetkovic");
        testUser.setPassword("lozinka123");
        testUser.setEmail("lazar@test.com");
        testUser.setPhoneNumber("+381601234567");

        testUserDTO = new UserDTO();
        testUserDTO.setUsername("lazar_cvetkovic");
        testUserDTO.setPassword("lozinka123");
        testUserDTO.setEmail("lazar@test.com");
        testUserDTO.setPhoneNumber("+381601234567");
    }

    @Test
    void testRegister_Success() {
        when(userService.createUser(any(User.class))).thenReturn(testUser);
        ResponseEntity<?> response = userController.register(testUserDTO);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }

    @Test
    void testRegister_Failure() {
        when(userService.createUser(any(User.class))).thenThrow(new RuntimeException("Greška pri registraciji"));
        ResponseEntity<?> response = userController.register(testUserDTO);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Greška pri registraciji", response.getBody());
    }

    @Test
    void testLogin_Success() {
        when(userService.login(anyString(), anyString())).thenReturn(testUser);
        ResponseEntity<?> response = userController.login(testUserDTO);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }

    @Test
    void testLogin_Failure() {
        when(userService.login(anyString(), anyString())).thenThrow(new RuntimeException("Pogrešni podaci"));
        ResponseEntity<?> response = userController.login(testUserDTO);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Pogrešni podaci", response.getBody());
    }
}