package com.projekat.weathertracker.dto;

import lombok.Data;
import java.util.List;

@Data
public class OpenWeatherResponse {

    private CoordData coord;
    private MainData main;
    private List<Weather> weather;
    private SysData sys;

    @Data
    public static class CoordData {
        private Double lat;
        private Double lon;
    }

    @Data
    public static class MainData {
        private Double temp;
        private Integer humidity;
    }

    @Data
    public static class Weather {
        private String description;
    }

    @Data
    public static class SysData {
        private String country;
    }
}