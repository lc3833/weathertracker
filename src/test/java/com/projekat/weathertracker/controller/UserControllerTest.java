package com.projekat.weathertracker.controller;

import com.projekat.weathertracker.model.User;
import com.projekat.weathertracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(10L);
        testUser.setUsername("lazar_cvetkovic");
        testUser.setEmail("lazar@test.com");
    }

    @Test
    void testRegisterUser_Success() {
        when(userService.createUser(testUser)).thenReturn(testUser);

        ResponseEntity<?> response = userController.register(testUser);

        assertNotNull(response);
        User body = (User) response.getBody();
        assertEquals("lazar_cvetkovic", body.getUsername());
        verify(userService).createUser(testUser);
    }
}