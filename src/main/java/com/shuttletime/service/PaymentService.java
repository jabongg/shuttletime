package com.shuttletime.service;

import com.shuttletime.model.dto.PaymentRequest;
import com.shuttletime.model.entity.BadmintonCourt;
import com.shuttletime.model.entity.Booking;
import com.shuttletime.model.entity.Payment;
import com.shuttletime.model.entity.User;
import com.shuttletime.repository.BookingRepository;
import com.shuttletime.repository.PaymentRepository;
import com.shuttletime.repository.UserRepository;
import com.shuttletime.repository.CourtRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private final PaymentRepository paymentRepo;
    @Autowired
    private final BookingRepository bookingRepo;
    @Autowired
    private final UserRepository userRepo;
    @Autowired
    private final CourtRepository courtRepo;
    @Autowired
    private final EntityManager entityManager;

    @Autowired
    public PaymentService(PaymentRepository paymentRepo,
                          BookingRepository bookingRepo,
                          UserRepository userRepo,
                          CourtRepository courtRepo,
                          EntityManager entityManager) {
        this.paymentRepo = paymentRepo;
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
        this.courtRepo = courtRepo;
        this.entityManager = entityManager;
    }

    @Transactional
    public Payment makePayment(PaymentRequest request) {
        // 1. Save payment
        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setCourtId(request.getCourtId());
        payment.setAmount(request.getAmount());
        payment.setStatus("SUCCESS"); // dummy success for now
        paymentRepo.save(payment);

        // 2. Fetch managed references
        User user = entityManager.getReference(User.class, request.getUserId());
        BadmintonCourt court = entityManager.getReference(BadmintonCourt.class, Long.valueOf(request.getCourtId()));

        // 3. Save booking linked with payment
        Booking booking = new Booking();
        booking.setUser(user); // ✅ managed entity
        booking.setBadmintonCourt(court); // ✅ managed entity
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setPayment(payment);

        bookingRepo.save(booking);

        return payment;
    }

    public Booking findBookingByPayment(Payment payment) {
        List<Booking> bookings = bookingRepo.findAll();
        for (Booking b : bookings) {
            if (b.getPayment() != null && b.getPayment().getId().equals(payment.getId())) {
                return b;
            }
        }
        throw new NoSuchElementException("No booking found for payment id: " + payment.getId());
    }
}
