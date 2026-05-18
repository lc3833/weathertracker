package com.projekat.weathertracker.repository;

import com.projekat.weathertracker.model.ForecastRecord;
import com.projekat.weathertracker.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ForecastRecordRepository extends JpaRepository<ForecastRecord, Long> {

    // NOVO: Metoda koja briše stare prognoze za određeni grad
    @Transactional
    void deleteByLocation(Location location);
}