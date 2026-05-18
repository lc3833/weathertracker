package com.projekat.weathertracker.service;

import com.projekat.weathertracker.model.User;
import com.projekat.weathertracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        // 1. Validacija Emaila (mora sadržati @ i tačku)
        if (user.getEmail() == null || !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new RuntimeException("Neispravan format email adrese!");
        }

        // 2. Validacija Lozinke (min 8 karaktera, bar jedan broj)
        if (user.getPassword() == null || user.getPassword().length() < 8 || !user.getPassword().matches(".*\\d.*")) {
            throw new RuntimeException("Lozinka mora imati najmanje 8 karaktera i sadržati bar jedan broj!");
        }

        // 3. Validacija Broja telefona (samo cifre, opcioni +, dužina 9-15)
        if (user.getPhoneNumber() == null || !user.getPhoneNumber().matches("^\\+?[0-9]{9,15}$")) {
            throw new RuntimeException("Broj telefona je obavezan i mora sadržati između 9 i 15 cifara!");
        }

        // 4. Provera da li username već postoji
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Korisničko ime već postoji!");
        }

        return userRepository.save(user);
    }

    public User login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt.get();
        }
        throw new RuntimeException("Pogrešno korisničko ime ili lozinka!");
    }
}