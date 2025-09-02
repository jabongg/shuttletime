package com.shuttletime.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @Embedded
    @JsonUnwrapped(enabled = false) // <- tells Jackson to serialize embedded fields instead of {}
    Location location;

    private String contactNumber;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference  // Handles forward serialization
    private List<BadmintonCourt> courts = new ArrayList<>();
}
