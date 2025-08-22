package com.shuttletime.model.entity;

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
    Location location;

    private String contactNumber;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BadmintonCourt> courts = new ArrayList<>();
}
