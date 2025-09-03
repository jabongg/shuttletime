package com.shuttletime.controller;

import com.shuttletime.model.dto.BookingRequest;
import com.shuttletime.model.dto.BookingResponse;
import com.shuttletime.model.entity.Booking;
import com.shuttletime.model.entity.Payment;
import com.shuttletime.repository.BookingRepository;
import com.shuttletime.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "http://localhost:5173") // allow React frontend
@Tag(name = "Booking API", description = "Operations related to badminton court bookings")
public class BookingController {

    @Autowired
    private final BookingService bookingService;

    @Autowired
    private final BookingRepository bookingRepository;

    public BookingController(BookingService bookingService, BookingRepository bookingRepository) {
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
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

    @Operation(
            summary = "Health Check",
            description = "Checks if the server is running and responding on port 8080"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Server is healthy and running"),
    })
    @GetMapping
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Server Health is ok. It's running fine on 8080");
    }


    @Operation(
            summary = "Get available slots for all courts in a venue",
            description = "Fetches all available 1-hour slots for every court in the given venue on a specific date."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Slots fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"Court A1\": [\"09:00\", \"10:00\", \"12:00\"],\n" +
                                    "  \"Court A2\": [\"09:00\", \"11:00\", \"14:00\"]\n" +
                                    "}")))
    })
    @GetMapping("available-slots/venue")
    public Map<String, List<String>> getAvailableSlotsForVenueByDate(
            @RequestParam Long venueId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return bookingService.getAvailableSlotsForVenueByDate(venueId, date);
    }


//    @GetMapping("/slots/venue")
//    public Map<String, Map<String, List<String>>> getSlotsForVenue(
//            @RequestParam Long venueId,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        return bookingService.getSlotsForVenue(venueId, date);
//    }

    @Operation(
            summary = "Get available slots for all courts in a venue",
            description = "Returns available and booked slots for each court in a venue on a given date",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Available slots fetched successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "404", description = "Venue not found")
            }
    )
    @GetMapping("/slots/venue/available")
    public ResponseEntity<Map<String, Map<String, List<String>>>> getAvailableSlotsForVenue(
            @Parameter(description = "Venue ID", required = true)
            @RequestParam Long venueId,

            @Parameter(description = "Booking date in format yyyy-MM-dd", required = true, example = "2025-08-25")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(bookingService.getAvailableSlotsForVenue(venueId, date));
    }

    @GetMapping("/by-payment/{paymentId}")
    public ResponseEntity<Booking> getBookingByPayment(@PathVariable Long paymentId) {
        Payment payment = new Payment();
        payment.setId(paymentId);

        return bookingService.findBookingByPayment(payment)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long bookingId) {
        return bookingService.getBookingDetails(bookingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

