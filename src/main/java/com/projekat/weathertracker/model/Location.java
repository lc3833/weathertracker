package com.projekat.weathertracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String country;
    private String zipCode;
    private Double latitude;
    private Double longitude;

    // NOVO: Veza sa korisnikom prema dijagramu
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}