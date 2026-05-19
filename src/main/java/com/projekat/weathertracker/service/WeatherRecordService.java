package com.projekat.weathertracker.service;

import com.projekat.weathertracker.dto.OpenWeatherResponse;
import com.projekat.weathertracker.model.Location;
import com.projekat.weathertracker.model.WeatherRecord;
import com.projekat.weathertracker.repository.WeatherRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WeatherRecordService {

    private final WeatherRecordRepository weatherRecordRepository;
    private final LocationService locationService;
    private final RestTemplate restTemplate;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    @Autowired
    public WeatherRecordService(WeatherRecordRepository weatherRecordRepository,
                                LocationService locationService,
                                RestTemplate restTemplate) {
        this.weatherRecordRepository = weatherRecordRepository;
        this.locationService = locationService;
        this.restTemplate = restTemplate;
    }

    public List<WeatherRecord> getAllRecords() {
        return weatherRecordRepository.findAll();
    }

    public void validateTemperature(Double temperature) {
        if (temperature < -273.15) {
            throw new RuntimeException("Fizički nemoguća temperatura! Ne može biti niža od apsolutne nule (-273.15°C).");
        }
    }

    public WeatherRecord fetchAndSaveWeatherForCity(String cityName) {
        Location location = locationService.getOrFetchLocation(cityName);

        String url = apiUrl + "?q=" + cityName + "&appid=" + apiKey + "&units=metric";
        OpenWeatherResponse response = restTemplate.getForObject(url, OpenWeatherResponse.class);

        if (response != null && response.getMain() != null) {
            Double temp = response.getMain().getTemp();

            validateTemperature(temp);
            WeatherRecord record = new WeatherRecord();
            record.setTemperatureC(temp);
            record.setHumidity(response.getMain().getHumidity());
            record.setDateTime(LocalDateTime.now());
            record.setLocation(location);

            if (response.getWeather() != null && !response.getWeather().isEmpty()) {
                record.setDescription(response.getWeather().get(0).getDescription());
            }

            return weatherRecordRepository.save(record);
        }

        throw new RuntimeException("Nije moguće preuzeti podatke o vremenu za grad: " + cityName);
    }
}