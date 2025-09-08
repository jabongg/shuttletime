package com.shuttletime.model.dto;

import lombok.Data;

@Data
public class PaymentsVerifyResponse {
    private String message;
    private Long bookingId;
}
