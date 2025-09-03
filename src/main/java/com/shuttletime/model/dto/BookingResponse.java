package com.shuttletime.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private String userName;
    private String venueName;
    private String courtName;
    private String bookingDate;
    private String slotTime;
    private Double amount;
}
