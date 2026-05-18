package com.projekat.weathertracker.controller;

import com.projekat.weathertracker.dto.CityStatsDTO;
import com.projekat.weathertracker.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/top-cities")
    public ResponseEntity<List<CityStatsDTO>> getTopCities() {
        return ResponseEntity.ok(statisticsService.getTop5MostSearchedCities());
    }
}