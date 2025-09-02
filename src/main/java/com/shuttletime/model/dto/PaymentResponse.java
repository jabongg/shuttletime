package com.shuttletime.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String transactionId;
    private String status;
    private double amount;
    private String courtId;
    private String userId;
    private Long bookingId;
}
