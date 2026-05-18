package com.projekat.weathertracker.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WeatherRecordTest {

    @Test
    void testWeatherRecordGettersAndSetters() {
        WeatherRecord record = new WeatherRecord();
        LocalDateTime now = LocalDateTime.now();

        record.setId(5L);
        record.setTemperatureC(22.5);
        record.setHumidity(50);
        record.setDescription("Clear Sky");
        record.setDateTime(now);

        assertEquals(5L, record.getId());
        assertEquals(22.5, record.getTemperatureC());
        assertEquals(50, record.getHumidity());
        assertEquals("Clear Sky", record.getDescription());
        assertNotNull(record.getDateTime());
    }
}