package com.projekat.weathertracker.repository;

import com.projekat.weathertracker.dto.CityStatsDTO;
import com.projekat.weathertracker.model.WeatherRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherRecordRepository extends JpaRepository<WeatherRecord, Long> {

    // NOVO: Specijalni upit koji spaja tabele, broji pretrage po gradu i sortira ih opadajuće
    @Query("SELECT new com.projekat.weathertracker.dto.CityStatsDTO(l.city, COUNT(w.id)) " +
            "FROM WeatherRecord w JOIN w.location l " +
            "GROUP BY l.city " +
            "ORDER BY COUNT(w.id) DESC")
    List<CityStatsDTO> findTopSearchedCities(Pageable pageable);
}