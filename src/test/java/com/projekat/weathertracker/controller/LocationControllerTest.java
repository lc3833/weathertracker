package com.projekat.weathertracker.controller;

import com.projekat.weathertracker.model.Location;
import com.projekat.weathertracker.service.LocationService;
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

public class LocationControllerTest {

    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    private Location dummyLocation;
    private List<Location> dummyLocations;

    @BeforeEach
    void setUp() {
        // Inicijalizacija mock objekata
        MockitoAnnotations.openMocks(this);

        dummyLocation = new Location();
        dummyLocation.setId(1L);
        dummyLocation.setCity("London");

        dummyLocations = Collections.singletonList(dummyLocation);
    }

    @Test
    void testGetAllLocations_Success() {
        // Podešavamo da naš lažni servis vrati lažnu listu lokacija
        when(locationService.getAllLocations()).thenReturn(dummyLocations);

        // Pokrećemo metodu kontrolera
        List<Location> response = locationController.getAllLocations();

        // Proveravamo da li je sve prošlo kako treba
        assertEquals(1, response.size());
        assertEquals("London", response.get(0).getCity());

        // Potvrđujemo da je metoda servisa zaista pozvana
        verify(locationService).getAllLocations();
    }
}