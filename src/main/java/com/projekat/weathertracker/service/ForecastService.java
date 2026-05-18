package com.projekat.weathertracker.service;

import com.projekat.weathertracker.dto.ForecastResponse;
import com.projekat.weathertracker.model.ForecastRecord;
import com.projekat.weathertracker.model.Location;
import com.projekat.weathertracker.repository.ForecastRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ForecastService {

    private final ForecastRecordRepository forecastRepository;
    private final LocationService locationService; // PROMENJENO
    private final RestTemplate restTemplate;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    @Autowired
    public ForecastService(ForecastRecordRepository forecastRepository,
                           LocationService locationService, // PROMENJENO
                           RestTemplate restTemplate) {
        this.forecastRepository = forecastRepository;
        this.locationService = locationService;
        this.restTemplate = restTemplate;
    }

    public List<ForecastRecord> fetchAndSaveForecast(String cityName) {
        Location location = locationService.getOrFetchLocation(cityName);

        String baseUrl = apiUrl.replace("/weather", "/forecast");
        String url = baseUrl + "?q=" + cityName + "&appid=" + apiKey + "&units=metric";

        ForecastResponse response = restTemplate.getForObject(url, ForecastResponse.class);
        List<ForecastRecord> savedRecords = new ArrayList<>();

        if (response != null && response.getList() != null) {

            // Pre upisa novih podataka, brišemo stare podatke za ovaj grad iz baze!
            forecastRepository.deleteByLocation(location);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // PROMENJENO: Sada uzimamo SVE podatke (svih 5 dana na svaka 3h), a ne samo prvih 5
            for (ForecastResponse.ForecastItem item : response.getList()) {
                ForecastRecord record = new ForecastRecord();
                record.setExpectedTempC(item.getMain().getTemp());
                record.setRainProbability(item.getPop());
                record.setForecastTime(LocalDateTime.parse(item.getDt_txt(), formatter));
                record.setLocation(location);
                savedRecords.add(forecastRepository.save(record));
            }
        }
        return savedRecords;
    }
}