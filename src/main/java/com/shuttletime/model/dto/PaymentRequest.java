package com.shuttletime.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private UUID userId;
    private String courtId;
    private double amount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
