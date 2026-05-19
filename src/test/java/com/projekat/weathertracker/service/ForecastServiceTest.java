package com.projekat.weathertracker.service;

import com.projekat.weathertracker.dto.ForecastResponse;
import com.projekat.weathertracker.model.ForecastRecord;
import com.projekat.weathertracker.model.Location;
import com.projekat.weathertracker.repository.ForecastRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForecastServiceTest {

    @Mock
    private ForecastRecordRepository forecastRepository;

    @Mock
    private LocationService locationService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ForecastService forecastService;

    private Location dummyLocation;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(forecastService, "apiUrl", "http://api.openweathermap.org/data/2.5/weather");
        ReflectionTestUtils.setField(forecastService, "apiKey", "test-api-key");

        dummyLocation = new Location();
        dummyLocation.setCity("Belgrade");
    }

    @Test
    void testFetchAndSaveForecast_NullResponse_ReturnsEmptyList() {
        when(locationService.getOrFetchLocation("Belgrade")).thenReturn(dummyLocation);
        when(restTemplate.getForObject(anyString(), eq(ForecastResponse.class))).thenReturn(null);

        List<ForecastRecord> result = forecastService.fetchAndSaveForecast("Belgrade");

        assertTrue(result.isEmpty());
        verify(forecastRepository, never()).deleteByLocation(any());
        verify(forecastRepository, never()).save(any());
    }

    @Test
    void testFetchAndSaveForecast_NullListInResponse_ReturnsEmptyList() {
        when(locationService.getOrFetchLocation("Belgrade")).thenReturn(dummyLocation);
        when(restTemplate.getForObject(anyString(), eq(ForecastResponse.class))).thenReturn(new ForecastResponse());

        List<ForecastRecord> result = forecastService.fetchAndSaveForecast("Belgrade");

        assertTrue(result.isEmpty());
        verify(forecastRepository, never()).deleteByLocation(any());
        verify(forecastRepository, never()).save(any());
    }

    @Test
    void testFetchAndSaveForecast_Success() {
        when(locationService.getOrFetchLocation("Belgrade")).thenReturn(dummyLocation);

        ForecastResponse mockResponse = new ForecastResponse();
        ForecastResponse.ForecastItem item = new ForecastResponse.ForecastItem();
        item.setDt_txt("2026-05-19 12:00:00");
        item.setPop(0.75);

        ForecastResponse.MainData mainData = new ForecastResponse.MainData();
        mainData.setTemp(22.5);
        item.setMain(mainData);

        mockResponse.setList(List.of(item));

        when(restTemplate.getForObject(anyString(), eq(ForecastResponse.class))).thenReturn(mockResponse);

        ForecastRecord savedRecord = new ForecastRecord();
        savedRecord.setExpectedTempC(22.5);
        savedRecord.setRainProbability(0.75);
        savedRecord.setForecastTime(LocalDateTime.of(2026, 5, 19, 12, 0, 0));

        when(forecastRepository.save(any(ForecastRecord.class))).thenReturn(savedRecord);

        List<ForecastRecord> result = forecastService.fetchAndSaveForecast("Belgrade");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(22.5, result.get(0).getExpectedTempC());
        verify(forecastRepository).deleteByLocation(dummyLocation);
        verify(forecastRepository).save(any(ForecastRecord.class));
    }
}