package com.shuttletime.model.dto;

import lombok.Data;

// DTO for request body
@Data
public class PaymentVerificationRequest {
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private String userId;
    private String courtId;
    private String startTime;
    private String endTime;
    private Double amount;

}
