package com.projekat.weathertracker.repository;

import com.projekat.weathertracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Nova metoda za pronalazak korisnika prilikom logovanja
    Optional<User> findByUsername(String username);
}