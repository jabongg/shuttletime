package com.shuttletime.controller;

// PaymentController.java
/**
 * @author jabongg
 */

import com.shuttletime.model.dto.PaymentRequest;
import com.shuttletime.model.dto.PaymentResponse;
import com.shuttletime.model.entity.Booking;
import com.shuttletime.model.entity.Payment;
import com.shuttletime.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(
        origins = {
                "http://localhost:5173",              // Local React dev frontend
                "https://shuttle-ui.vercel.app"      // Vercel frontend
        },
        allowedHeaders = "*",
        allowCredentials = "true"
)
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    // PaymentController.java
    @PostMapping
    public ResponseEntity<PaymentResponse> makePayment(@RequestBody PaymentRequest request) {
        Payment payment = service.makePayment(request);

        // Find booking by payment (to get bookingId)
        Booking booking = service.findBookingByPayment(payment);

        PaymentResponse response = new PaymentResponse(
                payment.getTransactionId(),
                payment.getStatus(),
                payment.getAmount(),
                payment.getCourtId(),
                payment.getUserId().toString(),
                booking.getId()
        );

        return ResponseEntity.ok(response);
    }

}
