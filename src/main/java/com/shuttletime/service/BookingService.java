package com.shuttletime.service;

import com.shuttletime.enums.BookingStatus;
import com.shuttletime.model.entity.BadmintonCourt;
import com.shuttletime.model.entity.Booking;
import com.shuttletime.model.entity.User;
import com.shuttletime.repository.BookingRepository;
import com.shuttletime.repository.CourtRepository;
import com.shuttletime.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookingService {

    private final BookingRepository bookingRepo;
    private final CourtRepository courtRepo;
    private final UserRepository userRepo;

    public BookingService(BookingRepository bookingRepo, CourtRepository courtRepo, UserRepository userRepo) {
        this.bookingRepo = bookingRepo;
        this.courtRepo = courtRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    // logic
    public Booking createBooking(Long courtId, Long userId, LocalDateTime start, LocalDateTime end) {
        BadmintonCourt court = courtRepo.findById(courtId).orElseThrow();
        User user = userRepo.findById(userId).orElseThrow();

        // check conflicts
        if (!bookingRepo.findConflictingBookings(courtId, start, end).isEmpty()) {
            throw new IllegalArgumentException("Court already booked in this slot");
        }

        Booking booking = new Booking();
        booking.setBadmintonCourt(court);
        booking.setUser(user);
        booking.setStartTime(start);
        booking.setEndTime(end);
        booking.setStatus(BookingStatus.PENDING);

        return bookingRepo.save(booking);
    }
}
