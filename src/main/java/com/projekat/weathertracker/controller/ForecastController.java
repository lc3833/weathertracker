package com.projekat.weathertracker.controller;

import com.projekat.weathertracker.model.ForecastRecord;
import com.projekat.weathertracker.service.ForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forecast")
public class ForecastController {

    private final ForecastService forecastService;

    @Autowired
    public ForecastController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @GetMapping("/{city}")
    public List<ForecastRecord> getForecast(@PathVariable String city) {
        return forecastService.fetchAndSaveForecast(city);
    }
}