package com.projekat.weathertracker.repository;

import com.projekat.weathertracker.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByCityIgnoreCase(String city);

    // NOVA LINIJA: Traži sve lokacije koje je pretražio određeni korisnik
    List<Location> findByUserId(Long userId);
}