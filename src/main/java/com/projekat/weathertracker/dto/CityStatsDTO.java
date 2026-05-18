package com.projekat.weathertracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CityStatsDTO {
    private String city;
    private Long searchCount;
}