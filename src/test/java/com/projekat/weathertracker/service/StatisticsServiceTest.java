package com.projekat.weathertracker.service;

import com.projekat.weathertracker.dto.CityStatsDTO;
import com.projekat.weathertracker.repository.WeatherRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StatisticsServiceTest {

    @Mock
    private WeatherRecordRepository weatherRecordRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTop5MostSearchedCities() {
        List<CityStatsDTO> mockList = Arrays.asList(
                new CityStatsDTO("Beograd", 10L),
                new CityStatsDTO("Novi Sad", 5L)
        );

        when(weatherRecordRepository.findTopSearchedCities(PageRequest.of(0, 5))).thenReturn(mockList);

        List<CityStatsDTO> result = statisticsService.getTop5MostSearchedCities();

        assertEquals(2, result.size());
        assertEquals("Beograd", result.get(0).getCity());
        assertEquals(10L, result.get(0).getSearchCount());
        verify(weatherRecordRepository, times(1)).findTopSearchedCities(PageRequest.of(0, 5));
    }
}