package com.shuttletime.controller;

import com.shuttletime.model.dto.TicketResponse;
import com.shuttletime.model.entity.Booking;
import com.shuttletime.repository.BookingRepository;
import com.shuttletime.service.BookingService;
import com.shuttletime.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class TicketController {

    private final BookingRepository bookingRepository;

    @Autowired
    private final TicketService ticketService;

    @Autowired
    private final BookingService bookingService;

    public TicketController(BookingRepository bookingRepository, TicketService ticketService, BookingService bookingService) {
        this.bookingRepository = bookingRepository;
        this.ticketService = ticketService;
        this.bookingService = bookingService;
    }

    @GetMapping("/{id}/ticket")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable Long id) {
        return (ResponseEntity<byte[]>) bookingRepository.findById(id)
                .map(booking -> {
                    try {
                        byte[] pdfBytes = ticketService.generateTicket(booking);
                        return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ticket-" + id + ".pdf")
                                .contentType(MediaType.APPLICATION_PDF)
                                .body(pdfBytes);
                    } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
