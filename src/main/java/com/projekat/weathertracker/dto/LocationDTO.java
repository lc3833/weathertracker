package com.projekat.weathertracker.dto;

import lombok.Data;

@Data
public class LocationDTO {
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
}