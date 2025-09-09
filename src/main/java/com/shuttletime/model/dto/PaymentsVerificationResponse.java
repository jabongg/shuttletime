package com.shuttletime.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentsVerificationResponse {
    private String transactionId;
    private String status;
    private double amount;
    private String courtId;
    private UUID userId;
    private Long bookingId;
    private String message;
}
