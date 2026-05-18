package com.projekat.weathertracker.controller;

import com.projekat.weathertracker.model.User;
import com.projekat.weathertracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User savedUser = userService.createUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // NOVI ENDPOINT ZA LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        try {
            User user = userService.login(loginUser.getUsername(), loginUser.getPassword());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Vraća tekst greške (npr. Pogrešna lozinka)
        }
    }
}