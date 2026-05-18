package com.projekat.weathertracker.controller;

import com.projekat.weathertracker.model.AirPollutionRecord;
import com.projekat.weathertracker.service.AirPollutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pollution")
public class AirPollutionController {

    private final AirPollutionService airPollutionService;

    @Autowired
    public AirPollutionController(AirPollutionService airPollutionService) {
        this.airPollutionService = airPollutionService;
    }

    @GetMapping("/current/{city}")
    public ResponseEntity<AirPollutionRecord> getCurrentPollution(@PathVariable String city) {
        try {
            AirPollutionRecord record = airPollutionService.fetchAndSavePollutionForCity(city);
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}