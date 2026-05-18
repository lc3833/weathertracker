package com.projekat.weathertracker.dto;

import lombok.Data;
import java.util.List;

@Data
public class ForecastResponse {
    private List<ForecastItem> list;

    @Data
    public static class ForecastItem {
        private Long dt;
        private MainData main;
        private Double pop; // Verovatnoća padavina
        private String dt_txt; // Datum i vreme kao tekst
    }

    @Data
    public static class MainData {
        private Double temp;
    }
}