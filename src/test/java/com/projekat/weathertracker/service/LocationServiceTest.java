package com.projekat.weathertracker.service;

import com.projekat.weathertracker.dto.OpenWeatherResponse;
import com.projekat.weathertracker.model.Location;
import com.projekat.weathertracker.model.User;
import com.projekat.weathertracker.repository.LocationRepository;
import com.projekat.weathertracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LocationService locationService;

    private Location location1;
    private Location location2;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(locationService, "apiUrl", "http://api.openweathermap.org/data/2.5/weather");
        ReflectionTestUtils.setField(locationService, "apiKey", "test-api-key");

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

    @Test
    void getOrFetchLocation_WhenCityExistsInDb_ShouldReturnFromDb() {
        when(locationRepository.findByCityIgnoreCase("Beograd")).thenReturn(Optional.of(location1));
        Location result = locationService.getOrFetchLocation("Beograd");
        assertEquals("Beograd", result.getCity());
        verify(restTemplate, never()).getForObject(anyString(), eq(OpenWeatherResponse.class));
    }

    @Test
    void getOrFetchLocation_WhenApiReturnsNull_ShouldThrowException() {
        when(locationRepository.findByCityIgnoreCase("NepoznatGrad")).thenReturn(Optional.empty());
        when(restTemplate.getForObject(anyString(), eq(OpenWeatherResponse.class))).thenReturn(null);
        assertThrows(RuntimeException.class, () -> locationService.getOrFetchLocation("NepoznatGrad"));
    }

    @Test
    void getOrFetchLocation_WhenCityNotInDbAndApiReturnsData_ShouldSaveAndReturn() {
        when(locationRepository.findByCityIgnoreCase("Nis")).thenReturn(Optional.empty());
        OpenWeatherResponse response = new OpenWeatherResponse();
        OpenWeatherResponse.CoordData coord = new OpenWeatherResponse.CoordData();
        coord.setLat(43.32);
        coord.setLon(21.89);
        response.setCoord(coord);
        OpenWeatherResponse.SysData sys = new OpenWeatherResponse.SysData();
        sys.setCountry("RS");
        response.setSys(sys);
        when(restTemplate.getForObject(anyString(), eq(OpenWeatherResponse.class))).thenReturn(response);
        Location savedLoc = new Location();
        savedLoc.setCity("Nis");
        when(locationRepository.save(any(Location.class))).thenReturn(savedLoc);
        Location result = locationService.getOrFetchLocation("Nis");
        assertEquals("Nis", result.getCity());
        verify(locationRepository).save(any(Location.class));
    }

    @Test
    void saveToUserHistory_ShouldSetUserAndSaveLocation() {
        User user = new User();
        user.setId(1L);
        when(locationRepository.findByCityIgnoreCase("Novi Sad")).thenReturn(Optional.of(location2));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(locationRepository.save(any(Location.class))).thenReturn(location2);
        Location result = locationService.saveToUserHistory("Novi Sad", 1L);
        assertNotNull(result.getUser());
        assertEquals(1L, result.getUser().getId());
        verify(locationRepository).save(location2);
    }

    @Test
    void getUserHistory_ShouldReturnLocationList() {
        when(locationRepository.findByUserId(1L)).thenReturn(List.of(location1));
        List<Location> result = locationService.getUserHistory(1L);
        assertFalse(result.isEmpty());
    }
}