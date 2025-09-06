package com.shuttletime.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID userId;

    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private String contactNumber;

    private String password;

    private String oauth2Provider;
}
