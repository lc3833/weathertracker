package com.projekat.weathertracker.service;

import com.projekat.weathertracker.dto.OpenWeatherResponse;
import com.projekat.weathertracker.model.Location;
import com.projekat.weathertracker.model.WeatherRecord;
import com.projekat.weathertracker.repository.WeatherRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class WeatherRecordServiceTest {

    @Mock
    private WeatherRecordRepository weatherRecordRepository;

    @Mock
    private LocationService locationService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherRecordService weatherRecordService;

    private Location dummyLocation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dummyLocation = new Location();
        dummyLocation.setCity("Belgrade");
    }

    @Test
    void testFetchAndSaveWeatherForCity_InvalidTemperature_ThrowsException() {
        OpenWeatherResponse mockResponse = new OpenWeatherResponse();
        OpenWeatherResponse.MainData mainData = new OpenWeatherResponse.MainData();
        mainData.setTemp(-300.00);
        mockResponse.setMain(mainData);

        when(locationService.getOrFetchLocation("Belgrade")).thenReturn(dummyLocation);
        when(restTemplate.getForObject(anyString(), eq(OpenWeatherResponse.class))).thenReturn(mockResponse);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            weatherRecordService.fetchAndSaveWeatherForCity("Belgrade");
        });

        assertTrue(exception.getMessage().contains("nemoguća temperatura"));
        verify(weatherRecordRepository, never()).save(any(WeatherRecord.class));
    }
}