package com.projekat.weathertracker.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationTest {

    @Test
    void testLocationGettersAndSetters() {
        Location location = new Location();
        location.setId(10L);
        location.setCity("Novi Sad");
        location.setCountry("RS");
        location.setLatitude(45.25);
        location.setLongitude(19.85);

        assertEquals(10L, location.getId());
        assertEquals("Novi Sad", location.getCity());
        assertEquals("RS", location.getCountry());
        assertEquals(45.25, location.getLatitude());
        assertEquals(19.85, location.getLongitude());
    }
}