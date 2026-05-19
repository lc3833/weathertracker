package com.projekat.weathertracker.controller;

import com.projekat.weathertracker.model.Location;
import com.projekat.weathertracker.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {

    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    private Location dummyLocation;
    private List<Location> dummyLocations;

    @BeforeEach
    void setUp() {
        dummyLocation = new Location();
        dummyLocation.setId(1L);
        dummyLocation.setCity("London");
        dummyLocations = Collections.singletonList(dummyLocation);
    }

    @Test
    void testGetAllLocations_Success() {
        when(locationService.getAllLocations()).thenReturn(dummyLocations);
        List<Location> response = locationController.getAllLocations();
        assertEquals(1, response.size());
        assertEquals("London", response.get(0).getCity());
        verify(locationService).getAllLocations();
    }

    @Test
    void testGetLocationById_Found() {
        when(locationService.getLocationById(1L)).thenReturn(Optional.of(dummyLocation));
        ResponseEntity<Location> response = locationController.getLocationById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dummyLocation, response.getBody());
    }

    @Test
    void testGetLocationById_NotFound() {
        when(locationService.getLocationById(99L)).thenReturn(Optional.empty());
        ResponseEntity<Location> response = locationController.getLocationById(99L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateLocation_Success() {
        com.projekat.weathertracker.dto.LocationDTO dto = new com.projekat.weathertracker.dto.LocationDTO();
        dto.setCity("London");

        when(locationService.saveLocation(any(Location.class))).thenReturn(dummyLocation);
        Location response = locationController.createLocation(dto);
        assertNotNull(response);
        assertEquals("London", response.getCity());
    }

    @Test
    void testDeleteLocation_Success() {
        doNothing().when(locationService).deleteLocation(1L);
        ResponseEntity<Void> response = locationController.deleteLocation(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(locationService).deleteLocation(1L);
    }

    @Test
    void testSaveHistory_Success() {
        when(locationService.saveToUserHistory("London", 1L)).thenReturn(dummyLocation);
        ResponseEntity<Location> response = locationController.saveHistory("London", 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dummyLocation, response.getBody());
    }

    @Test
    void testGetHistory_Success() {
        when(locationService.getUserHistory(1L)).thenReturn(dummyLocations);
        ResponseEntity<List<Location>> response = locationController.getHistory(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }
}