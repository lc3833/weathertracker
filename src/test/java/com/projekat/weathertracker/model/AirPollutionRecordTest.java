package com.projekat.weathertracker.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AirPollutionRecordTest {

    @Test
    void testAirPollutionGettersAndSetters() {
        AirPollutionRecord record = new AirPollutionRecord();
        record.setId(3L);
        record.setAqi(2);
        record.setCo(200.5);
        record.setNo2(1.2);
        record.setPm25(15.4);

        assertEquals(3L, record.getId());
        assertEquals(2, record.getAqi());
        assertEquals(200.5, record.getCo());
        assertEquals(1.2, record.getNo2());
        assertEquals(15.4, record.getPm25());
    }
}