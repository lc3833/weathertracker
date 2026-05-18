package com.projekat.weathertracker.controller;

import com.projekat.weathertracker.dto.CityStatsDTO;
import com.projekat.weathertracker.service.StatisticsService;
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
import static org.mockito.Mockito.*;

public class StatisticsControllerTest {

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private StatisticsController statisticsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTopCities() {
        CityStatsDTO dto = new CityStatsDTO("Nis", 8L);
        when(statisticsService.getTop5MostSearchedCities()).thenReturn(Collections.singletonList(dto));

        ResponseEntity<List<CityStatsDTO>> response = statisticsController.getTopCities();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Nis", response.getBody().get(0).getCity());
        verify(statisticsService, times(1)).getTop5MostSearchedCities();
    }
}