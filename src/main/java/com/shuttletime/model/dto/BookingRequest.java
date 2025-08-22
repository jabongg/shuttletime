package com.shuttletime.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequest {
    private Long courtId;
    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
