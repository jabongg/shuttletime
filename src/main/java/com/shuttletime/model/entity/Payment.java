package com.shuttletime.model.entity;

// Payment.java
import com.shuttletime.enums.PaymentStatus;
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

    // ✅ Razorpay-specific fields
    @Column(unique = true)
    private String razorpayOrderId;

    @Column(unique = true)
    private String razorpayPaymentId;

    private String razorpaySignature;

    @Enumerated(EnumType.STRING)
    private PaymentStatus razorpayStatus = PaymentStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();


    public Payment() {
        this.transactionId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private Booking booking;

    // getters and setters
}
