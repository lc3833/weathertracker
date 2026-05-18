package com.projekat.weathertracker.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class ForecastRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime forecastTime;
    private Double expectedTempC;
    private Double rainProbability;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
}