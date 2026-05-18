package com.projekat.weathertracker.controller;

import com.projekat.weathertracker.model.WeatherRecord;
import com.projekat.weathertracker.service.WeatherRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeatherRecordControllerTest {

    @Mock
    private WeatherRecordService weatherRecordService;

    @InjectMocks
    private WeatherRecordController weatherRecordController;

    private WeatherRecord dummyWeather;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dummyWeather = new WeatherRecord();
        dummyWeather.setTemperatureC(22.5);
        dummyWeather.setDescription("Clear sky");
    }

    @Test
    void testGetAllRecords() {
        when(weatherRecordService.getAllRecords()).thenReturn(Collections.singletonList(dummyWeather));

        List<WeatherRecord> response = weatherRecordController.getAllRecords();

        assertEquals(1, response.size());
        assertEquals(22.5, response.get(0).getTemperatureC());
        verify(weatherRecordService).getAllRecords();
    }

    @Test
    void testGetCurrentWeather_Success() {
        when(weatherRecordService.fetchAndSaveWeatherForCity("Novi Sad")).thenReturn(dummyWeather);

        ResponseEntity<WeatherRecord> response = weatherRecordController.getCurrentWeather("Novi Sad");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Clear sky", response.getBody().getDescription());
        verify(weatherRecordService).fetchAndSaveWeatherForCity("Novi Sad");
    }
}