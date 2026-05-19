package com.projekat.weathertracker.service;

import com.projekat.weathertracker.dto.OpenWeatherResponse;
import com.projekat.weathertracker.model.Location;
import com.projekat.weathertracker.repository.LocationRepository;
import com.projekat.weathertracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    @Autowired
    public LocationService(LocationRepository locationRepository, RestTemplate restTemplate, UserRepository userRepository) {
        this.locationRepository = locationRepository;
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
    }

    public Location getOrFetchLocation(String cityName) {
        return locationRepository.findByCityIgnoreCase(cityName)
                .orElseGet(() -> {
                    String url = apiUrl + "?q=" + cityName + "&appid=" + apiKey + "&units=metric";
                    OpenWeatherResponse response = restTemplate.getForObject(url, OpenWeatherResponse.class);

                    if (response != null && response.getCoord() != null) {
                        Location newLocation = new Location();
                        newLocation.setCity(cityName);
                        newLocation.setCountry(response.getSys() != null ? response.getSys().getCountry() : "N/A");
                        newLocation.setLatitude(response.getCoord().getLat());
                        newLocation.setLongitude(response.getCoord().getLon());
                        return locationRepository.save(newLocation);
                    }
                    throw new RuntimeException("Grad nije pronađen na OpenWeather servisu: " + cityName);
                });
    }

    public Location saveToUserHistory(String cityName, Long userId) {
        Location location = getOrFetchLocation(cityName);
        userRepository.findById(userId).ifPresent(user -> {
            location.setUser(user);
            locationRepository.save(location);
        });
        return location;
    }

    public List<Location> getUserHistory(Long userId) {
        return locationRepository.findByUserId(userId);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }
}