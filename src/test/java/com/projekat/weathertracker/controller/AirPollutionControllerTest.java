package com.projekat.weathertracker.controller;

import com.projekat.weathertracker.model.AirPollutionRecord;
import com.projekat.weathertracker.service.AirPollutionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AirPollutionControllerTest {

    @Mock
    private AirPollutionService airPollutionService;

    @InjectMocks
    private AirPollutionController airPollutionController;

    private AirPollutionRecord dummyRecord;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dummyRecord = new AirPollutionRecord();
        dummyRecord.setAqi(2);
        dummyRecord.setPm25(15.5);
    }

    @Test
    void testGetCurrentPollution_Success() {
        when(airPollutionService.fetchAndSavePollutionForCity("Nis")).thenReturn(dummyRecord);

        ResponseEntity<AirPollutionRecord> response = airPollutionController.getCurrentPollution("Nis");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getAqi());
        verify(airPollutionService).fetchAndSavePollutionForCity("Nis");
    }
}