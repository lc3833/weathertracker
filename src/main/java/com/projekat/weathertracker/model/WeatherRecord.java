package com.projekat.weathertracker.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class WeatherRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double temperatureC;
    private LocalDateTime dateTime;
    private Integer humidity;
    private String description;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
}