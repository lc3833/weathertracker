package com.projekat.weathertracker.service;

import com.projekat.weathertracker.dto.AirPollutionResponse;
import com.projekat.weathertracker.model.AirPollutionRecord;
import com.projekat.weathertracker.model.Location;
import com.projekat.weathertracker.repository.AirPollutionRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirPollutionServiceTest {

    @Mock
    private AirPollutionRecordRepository pollutionRepository;

    @Mock
    private LocationService locationService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AirPollutionService airPollutionService;

    private Location validLocation;
    private Location invalidLocation;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(airPollutionService, "apiUrl", "http://api.openweathermap.org/data/2.5/weather");
        ReflectionTestUtils.setField(airPollutionService, "apiKey", "test-api-key");

        validLocation = new Location();
        validLocation.setCity("Belgrade");
        validLocation.setLatitude(44.81);
        validLocation.setLongitude(20.46);

        invalidLocation = new Location();
        invalidLocation.setCity("Unknown");
    }

    @Test
    void testFetchAndSavePollutionForCity_MissingCoordinates_ThrowsException() {
        when(locationService.getOrFetchLocation("Unknown")).thenReturn(invalidLocation);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                airPollutionService.fetchAndSavePollutionForCity("Unknown")
        );
        assertTrue(exception.getMessage().contains("nema upisane koordinate"));
        verify(restTemplate, never()).getForObject(anyString(), eq(AirPollutionResponse.class));
    }

    @Test
    void testFetchAndSavePollutionForCity_NullApiResponse_ThrowsException() {
        when(locationService.getOrFetchLocation("Belgrade")).thenReturn(validLocation);
        when(restTemplate.getForObject(anyString(), eq(AirPollutionResponse.class))).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                airPollutionService.fetchAndSavePollutionForCity("Belgrade")
        );
        assertTrue(exception.getMessage().contains("Nije moguće preuzeti podatke"));
    }

    @Test
    void testFetchAndSavePollutionForCity_EmptyListResponse_ThrowsException() {
        AirPollutionResponse emptyResponse = new AirPollutionResponse();
        emptyResponse.setList(Collections.emptyList());

        when(locationService.getOrFetchLocation("Belgrade")).thenReturn(validLocation);
        when(restTemplate.getForObject(anyString(), eq(AirPollutionResponse.class))).thenReturn(emptyResponse);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                airPollutionService.fetchAndSavePollutionForCity("Belgrade")
        );
        assertTrue(exception.getMessage().contains("Nije moguće preuzeti podatke"));
    }

    @Test
    void testFetchAndSavePollutionForCity_Success() {
        AirPollutionResponse mockResponse = new AirPollutionResponse();

        AirPollutionResponse.Main mainData = new AirPollutionResponse.Main();
        mainData.setAqi(3);

        AirPollutionResponse.Components componentsData = new AirPollutionResponse.Components();
        componentsData.setCo(200.5);
        componentsData.setNo2(15.2);
        componentsData.setPm2_5(12.5);

        AirPollutionResponse.PollutionData pollutionData = new AirPollutionResponse.PollutionData();
        pollutionData.setMain(mainData);
        pollutionData.setComponents(componentsData);

        mockResponse.setList(List.of(pollutionData));

        when(locationService.getOrFetchLocation("Belgrade")).thenReturn(validLocation);
        when(restTemplate.getForObject(anyString(), eq(AirPollutionResponse.class))).thenReturn(mockResponse);

        AirPollutionRecord savedRecord = new AirPollutionRecord();
        savedRecord.setAqi(3);
        savedRecord.setCo(200.5);
        when(pollutionRepository.save(any(AirPollutionRecord.class))).thenReturn(savedRecord);

        AirPollutionRecord result = airPollutionService.fetchAndSavePollutionForCity("Belgrade");

        assertNotNull(result);
        assertEquals(3, result.getAqi());
        assertEquals(200.5, result.getCo());
        verify(pollutionRepository).save(any(AirPollutionRecord.class));
    }
}