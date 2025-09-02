package com.shuttletime.model.entity;

// Payment.java
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private String status;
    private double amount;
    private String courtId;
    @Column(name = "user_id", columnDefinition = "UUID")
    private UUID userId;   // Changed to UUID

    private LocalDateTime createdAt;

    public Payment() {
        this.transactionId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private Booking booking;

    // getters and setters
}
