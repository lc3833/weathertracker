package com.projekat.weathertracker.controller;

import com.projekat.weathertracker.model.ForecastRecord;
import com.projekat.weathertracker.service.ForecastService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ForecastControllerTest {

    @Mock
    private ForecastService forecastService;

    @InjectMocks
    private ForecastController forecastController;

    private ForecastRecord dummyForecast;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dummyForecast = new ForecastRecord();
        dummyForecast.setExpectedTempC(25.5);
        dummyForecast.setRainProbability(0.2);
    }

    @Test
    void testGetForecast_Success() {
        when(forecastService.fetchAndSaveForecast("Nis")).thenReturn(Collections.singletonList(dummyForecast));

        List<ForecastRecord> response = forecastController.getForecast("Nis");

        assertEquals(1, response.size());
        assertEquals(25.5, response.get(0).getExpectedTempC());
        verify(forecastService).fetchAndSaveForecast("Nis");
    }
}