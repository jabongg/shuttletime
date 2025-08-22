package com.shuttletime.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class BadmintonCourt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courtName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;
}
