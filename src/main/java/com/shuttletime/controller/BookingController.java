package com.shuttletime.controller;

import com.shuttletime.model.dto.BookingRequest;
import com.shuttletime.model.entity.Booking;
import com.shuttletime.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Booking API", description = "Operations related to badminton court bookings")
public class BookingController {

    @Autowired
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(
            summary = "Create a new booking",
            description = "Book a badminton court for a specific user and time slot.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Booking created successfully",
                            content = @Content(schema = @Schema(implementation = Booking.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid booking request",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Court or User not found",
                            content = @Content
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest req) {
        Booking booking = bookingService.createBooking(
                req.getCourtId(),
                req.getUserId(),
                req.getStartTime(),
                req.getEndTime()
        );
        return ResponseEntity.ok(booking);
    }
}

