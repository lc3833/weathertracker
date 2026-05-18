package com.projekat.weathertracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "app_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    // NOVO POLJE
    private String phoneNumber;
}