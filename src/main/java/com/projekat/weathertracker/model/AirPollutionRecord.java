package com.projekat.weathertracker.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class AirPollutionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer aqi; // Air Quality Index
    private Double co;
    private Double no2;
    private Double pm25;
    private LocalDateTime measuredAt;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
}