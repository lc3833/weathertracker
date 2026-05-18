package com.projekat.weathertracker.repository;

import com.projekat.weathertracker.model.AirPollutionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirPollutionRecordRepository extends JpaRepository<AirPollutionRecord, Long> {
}