package com.projekat.weathertracker.service;

import com.projekat.weathertracker.dto.CityStatsDTO;
import com.projekat.weathertracker.repository.WeatherRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    private final WeatherRecordRepository weatherRecordRepository;

    @Autowired
    public StatisticsService(WeatherRecordRepository weatherRecordRepository) {
        this.weatherRecordRepository = weatherRecordRepository;
    }

    public List<CityStatsDTO> getTop5MostSearchedCities() {

        return weatherRecordRepository.findTopSearchedCities(PageRequest.of(0, 5));
    }
}