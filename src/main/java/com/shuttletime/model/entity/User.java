package com.shuttletime.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String username;
    @Column(nullable = false, unique = true)
    String email;
    String contactNumber;
}
