package com.projekat.weathertracker.service;

import com.projekat.weathertracker.model.User;
import com.projekat.weathertracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User validUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validUser = new User();
        validUser.setUsername("lazar_test");
        validUser.setEmail("lazar@test.com");
        validUser.setPassword("lozinka123");
        validUser.setPhoneNumber("+381601234567");
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.findByUsername("lazar_test")).thenReturn(Optional.empty());
        when(userRepository.save(validUser)).thenReturn(validUser);

        User result = userService.createUser(validUser);

        assertNotNull(result);
        assertEquals("lazar_test", result.getUsername());
        verify(userRepository).save(validUser);
    }

    @Test
    void testCreateUser_InvalidEmail_ThrowsException() {
        validUser.setEmail("lazarbezludogA.com");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(validUser));
        assertTrue(exception.getMessage().contains("Neispravan format email adrese"));
    }

    @Test
    void testCreateUser_InvalidPassword_ThrowsException() {
        validUser.setPassword("kratka");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(validUser));
        assertTrue(exception.getMessage().contains("najmanje 8 karaktera"));
    }

    @Test
    void testCreateUser_InvalidPhoneNumber_ThrowsException() {
        validUser.setPhoneNumber("123");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(validUser));
        assertTrue(exception.getMessage().contains("između 9 i 15 cifara"));
    }

    @Test
    void testCreateUser_UsernameAlreadyExists_ThrowsException() {
        when(userRepository.findByUsername("lazar_test")).thenReturn(Optional.of(validUser));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(validUser));
        assertTrue(exception.getMessage().contains("već postoji"));
    }
}