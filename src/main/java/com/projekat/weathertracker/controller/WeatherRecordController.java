package com.projekat.weathertracker.controller;

import com.projekat.weathertracker.model.WeatherRecord;
import com.projekat.weathertracker.service.WeatherRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class WeatherRecordController {

    private final WeatherRecordService weatherRecordService;

    @Autowired
    public WeatherRecordController(WeatherRecordService weatherRecordService) {
        this.weatherRecordService = weatherRecordService;
    }

    @GetMapping
    public List<WeatherRecord> getAllRecords() {
        return weatherRecordService.getAllRecords();
    }

    // OVO JE NAŠ NOVI IPHONE-STYLE ENDPOINT
    @GetMapping("/current/{city}")
    public ResponseEntity<WeatherRecord> getCurrentWeather(@PathVariable String city) {
        try {
            WeatherRecord record = weatherRecordService.fetchAndSaveWeatherForCity(city);
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}