package com.projekat.weathertracker.repository;

import com.projekat.weathertracker.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByCityIgnoreCase(String city);
    List<Location> findByUserId(Long userId);
}