package com.projekat.weathertracker.service;

import com.projekat.weathertracker.dto.OpenWeatherResponse;
import com.projekat.weathertracker.model.Location;
import com.projekat.weathertracker.model.WeatherRecord;
import com.projekat.weathertracker.repository.WeatherRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherRecordServiceTest {

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
        ReflectionTestUtils.setField(weatherRecordService, "apiUrl", "http://api.openweathermap.org/data/2.5/weather");
        ReflectionTestUtils.setField(weatherRecordService, "apiKey", "test-api-key");

        dummyLocation = new Location();
        dummyLocation.setCity("Belgrade");
    }

    @Test
    void testGetAllRecords() {
        when(weatherRecordRepository.findAll()).thenReturn(List.of(new WeatherRecord()));
        List<WeatherRecord> result = weatherRecordService.getAllRecords();
        assertFalse(result.isEmpty());
        verify(weatherRecordRepository).findAll();
    }

    @Test
    void testFetchAndSaveWeatherForCity_InvalidTemperature_ThrowsException() {
        OpenWeatherResponse mockResponse = new OpenWeatherResponse();
        OpenWeatherResponse.MainData mainData = new OpenWeatherResponse.MainData();
        mainData.setTemp(-300.00);
        mockResponse.setMain(mainData);

        when(locationService.getOrFetchLocation("Belgrade")).thenReturn(dummyLocation);
        when(restTemplate.getForObject(anyString(), eq(OpenWeatherResponse.class))).thenReturn(mockResponse);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                weatherRecordService.fetchAndSaveWeatherForCity("Belgrade")
        );
        assertTrue(exception.getMessage().contains("nemoguća temperatura"));
        verify(weatherRecordRepository, never()).save(any(WeatherRecord.class));
    }

    @Test
    void testFetchAndSaveWeatherForCity_NullResponse_ThrowsException() {
        when(locationService.getOrFetchLocation("Belgrade")).thenReturn(dummyLocation);
        when(restTemplate.getForObject(anyString(), eq(OpenWeatherResponse.class))).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                weatherRecordService.fetchAndSaveWeatherForCity("Belgrade")
        );
        assertTrue(exception.getMessage().contains("Nije moguće preuzeti podatke"));
        verify(weatherRecordRepository, never()).save(any(WeatherRecord.class));
    }

    @Test
    void testFetchAndSaveWeatherForCity_Success() {
        OpenWeatherResponse mockResponse = new OpenWeatherResponse();
        OpenWeatherResponse.MainData mainData = new OpenWeatherResponse.MainData();
        mainData.setTemp(25.0);
        mainData.setHumidity(50);
        mockResponse.setMain(mainData);

        OpenWeatherResponse.Weather weatherItem = new OpenWeatherResponse.Weather();
        weatherItem.setDescription("clear sky");
        mockResponse.setWeather(List.of(weatherItem));

        when(locationService.getOrFetchLocation("Belgrade")).thenReturn(dummyLocation);
        when(restTemplate.getForObject(anyString(), eq(OpenWeatherResponse.class))).thenReturn(mockResponse);

        WeatherRecord savedRecord = new WeatherRecord();
        savedRecord.setDescription("clear sky");
        when(weatherRecordRepository.save(any(WeatherRecord.class))).thenReturn(savedRecord);

        WeatherRecord result = weatherRecordService.fetchAndSaveWeatherForCity("Belgrade");

        assertNotNull(result);
        assertEquals("clear sky", result.getDescription());
        verify(weatherRecordRepository).save(any(WeatherRecord.class));
    }
}