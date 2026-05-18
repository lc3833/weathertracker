package com.projekat.weathertracker.dto;

import lombok.Data;
import java.util.List;

@Data
public class AirPollutionResponse {

    private List<PollutionData> list;

    @Data
    public static class PollutionData {
        private Main main;
        private Components components;
    }

    @Data
    public static class Main {
        private Integer aqi; // Air Quality Index (1 = Dobro, 5 = Veoma loše)
    }

    @Data
    public static class Components {
        private Double co;
        private Double no2;
        private Double pm2_5; // PM2.5 čestice
    }
}