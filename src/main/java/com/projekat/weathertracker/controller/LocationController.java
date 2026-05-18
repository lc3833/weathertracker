package com.projekat.weathertracker.controller;

import com.projekat.weathertracker.model.Location;
import com.projekat.weathertracker.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        return locationService.getLocationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Location createLocation(@RequestBody Location location) {
        return locationService.saveLocation(location);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }

    // NOVO: Čuvanje u istoriju
    @PostMapping("/history")
    public ResponseEntity<Location> saveHistory(@RequestParam String city, @RequestParam Long userId) {
        return ResponseEntity.ok(locationService.saveToUserHistory(city, userId));
    }

    // NOVO: Čitanje istorije
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Location>> getHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(locationService.getUserHistory(userId));
    }
}