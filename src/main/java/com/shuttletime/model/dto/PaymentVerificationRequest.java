package com.shuttletime.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

// DTO for request body
@Data
public class PaymentVerificationRequest {
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private UUID userId;
    private String courtId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double amount;

}
