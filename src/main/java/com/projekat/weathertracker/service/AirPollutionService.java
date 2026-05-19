package com.projekat.weathertracker.service;

import com.projekat.weathertracker.dto.AirPollutionResponse;
import com.projekat.weathertracker.model.AirPollutionRecord;
import com.projekat.weathertracker.model.Location;
import com.projekat.weathertracker.repository.AirPollutionRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class AirPollutionService {

    private final AirPollutionRecordRepository pollutionRepository;
    private final LocationService locationService;
    private final RestTemplate restTemplate;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    @Autowired
    public AirPollutionService(AirPollutionRecordRepository pollutionRepository,
                               LocationService locationService,
                               RestTemplate restTemplate) {
        this.pollutionRepository = pollutionRepository;
        this.locationService = locationService;
        this.restTemplate = restTemplate;
    }

    public AirPollutionRecord fetchAndSavePollutionForCity(String cityName) {
        Location location = locationService.getOrFetchLocation(cityName);

        if (location.getLatitude() == null || location.getLongitude() == null) {
            throw new RuntimeException("Lokacija nema upisane koordinate.");
        }

        String baseUrl = apiUrl.replace("/weather", "/air_pollution");
        String url = baseUrl + "?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&appid=" + apiKey;

        AirPollutionResponse response = restTemplate.getForObject(url, AirPollutionResponse.class);

        if (response != null && response.getList() != null && !response.getList().isEmpty()) {
            AirPollutionResponse.PollutionData data = response.getList().get(0);
            AirPollutionRecord record = new AirPollutionRecord();
            record.setAqi(data.getMain().getAqi());
            record.setCo(data.getComponents().getCo());
            record.setNo2(data.getComponents().getNo2());
            record.setPm25(data.getComponents().getPm2_5());
            record.setMeasuredAt(LocalDateTime.now());
            record.setLocation(location);

            return pollutionRepository.save(record);
        }

        throw new RuntimeException("Nije moguće preuzeti podatke o zagađenju.");
    }
}