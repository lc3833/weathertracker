package com.projekat.weathertracker.service;

import com.projekat.weathertracker.model.User;
import com.projekat.weathertracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User validUser;

    @BeforeEach
    void setUp() {
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
    void testCreateUser_NullEmail_ThrowsException() {
        validUser.setEmail(null);
        assertThrows(RuntimeException.class, () -> userService.createUser(validUser));
    }

    @Test
    void testCreateUser_InvalidEmail_ThrowsException() {
        validUser.setEmail("neispravan.email.com");
        assertThrows(RuntimeException.class, () -> userService.createUser(validUser));
    }

    @Test
    void testCreateUser_NullPassword_ThrowsException() {
        validUser.setPassword(null);
        assertThrows(RuntimeException.class, () -> userService.createUser(validUser));
    }

    @Test
    void testCreateUser_InvalidPassword_ThrowsException() {
        validUser.setPassword("kratka");
        assertThrows(RuntimeException.class, () -> userService.createUser(validUser));
    }

    @Test
    void testCreateUser_NullPhoneNumber_ThrowsException() {
        validUser.setPhoneNumber(null);
        assertThrows(RuntimeException.class, () -> userService.createUser(validUser));
    }

    @Test
    void testCreateUser_InvalidPhoneNumber_ThrowsException() {
        validUser.setPhoneNumber("123");
        assertThrows(RuntimeException.class, () -> userService.createUser(validUser));
    }

    @Test
    void testCreateUser_UsernameAlreadyExists_ThrowsException() {
        when(userRepository.findByUsername("lazar_test")).thenReturn(Optional.of(validUser));
        assertThrows(RuntimeException.class, () -> userService.createUser(validUser));
    }

    @Test
    void testLogin_Success() {
        when(userRepository.findByUsername("lazar_test")).thenReturn(Optional.of(validUser));

        User result = userService.login("lazar_test", "lozinka123");

        assertNotNull(result);
        assertEquals("lazar_test", result.getUsername());
    }

    @Test
    void testLogin_WrongPassword_ThrowsException() {
        when(userRepository.findByUsername("lazar_test")).thenReturn(Optional.of(validUser));

        assertThrows(RuntimeException.class, () -> userService.login("lazar_test", "pogresnaLozinka1"));
    }

    @Test
    void testLogin_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername("nepoznat_user")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.login("nepoznat_user", "lozinka123"));
    }
}