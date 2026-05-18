package com.projekat.weathertracker.service;

import com.projekat.weathertracker.model.Location;
import com.projekat.weathertracker.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LocationService locationService;

    private Location location1;
    private Location location2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        location1 = new Location();
        location1.setId(1L);
        location1.setCity("Beograd");

        location2 = new Location();
        location2.setId(2L);
        location2.setCity("Novi Sad");
    }

    @Test
    void testGetAllLocations() {
        when(locationRepository.findAll()).thenReturn(Arrays.asList(location1, location2));

        List<Location> result = locationService.getAllLocations();

        assertEquals(2, result.size());
        verify(locationRepository, times(1)).findAll();
    }

    @Test
    void testGetLocationById_Found() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location1));

        Optional<Location> result = locationService.getLocationById(1L);

        assertTrue(result.isPresent());
        assertEquals("Beograd", result.get().getCity());
    }

    @Test
    void testSaveLocation() {
        when(locationRepository.save(location1)).thenReturn(location1);

        Location result = locationService.saveLocation(location1);

        assertNotNull(result);
        assertEquals("Beograd", result.getCity());
    }
}