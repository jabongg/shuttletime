package com.shuttletime.service;

import com.shuttletime.enums.BookingStatus;
import com.shuttletime.model.dto.BookingResponse;
import com.shuttletime.model.entity.*;
import com.shuttletime.repository.BookingRepository;
import com.shuttletime.repository.CourtRepository;
import com.shuttletime.repository.UserRepository;
import com.shuttletime.repository.VenueRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private final BookingRepository bookingRepo;

    @Autowired
    private final CourtRepository courtRepo;

    @Autowired
    private final UserRepository userRepo;

    @Autowired
    private final VenueRepository venueRepo;


    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");
    private static final LocalTime OPEN_TIME = LocalTime.of(9, 0);
    private static final LocalTime CLOSE_TIME = LocalTime.of(22, 0);

    public BookingService(BookingRepository bookingRepo, CourtRepository courtRepo,
                          UserRepository userRepo, VenueRepository venueRepo) {
        this.bookingRepo = bookingRepo;
        this.courtRepo = courtRepo;
        this.userRepo = userRepo;
        this.venueRepo = venueRepo;
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
        booking.setStatus(BookingStatus.CONFIRMED);

        return bookingRepo.save(booking);
    }


    private List<LocalTime> generateHourlySlots() {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(22, 0);

        while (start.isBefore(end)) {
            slots.add(start);
            start = start.plusHours(1);
        }
        return slots;
    }



    public Map<String, List<String>> getAvailableSlotsForVenueByDate(Long venueId, LocalDate date) {
        Map<String, List<String>> availableSlotsMap = new LinkedHashMap<>();

        // ✅ Fetch venue and courts
        Venue venue = venueRepo.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found"));

        List<BadmintonCourt> courts = venue.getCourts();

        // ✅ Fetch bookings for that venue & date
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        List<Booking> bookings = bookingRepo.findBookingsByVenueAndDate(venueId, startOfDay, endOfDay);

        // ✅ Group bookings by courtId
        Map<Long, List<Booking>> bookingsByCourt = bookings.stream()
                .collect(Collectors.groupingBy(b -> b.getBadmintonCourt().getId()));

        // ✅ Define standard 1-hour slots
        List<LocalTime> allSlots = generateHourlySlots();

        // ✅ Check availability for each court
        for (BadmintonCourt court : courts) {
            Set<LocalTime> bookedSlots = bookingsByCourt
                    .getOrDefault(court.getId(), Collections.emptyList())
                    .stream()
                    .map(b -> b.getStartTime().toLocalTime())
                    .collect(Collectors.toSet());

            List<String> availableSlots = allSlots.stream()
                    .filter(slot -> !bookedSlots.contains(slot))
                    .map(LocalTime::toString)
                    .collect(Collectors.toList());

            availableSlotsMap.put(court.getCourtName(), availableSlots);
        }

        return availableSlotsMap;
    }


    /** Get available and booked slots for all courts in a venue on a given date. */
    public Map<String, Map<String, List<String>>> getAvailableSlotsForVenue(Long venueId, LocalDate date) {
        Venue venue = venueRepo.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found"));

        Map<String, Map<String, List<String>>> response = new HashMap<>();

        for (BadmintonCourt court : venue.getCourts()) {
            List<Booking> bookingsForThisCourt = bookingRepo
                    .findByBadmintonCourt_IdAndStartTimeBetween(
                            court.getId(),
                            date.atTime(OPEN_TIME),
                            date.atTime(CLOSE_TIME));

            Set<String> bookedSlots = bookingsForThisCourt.stream()
                    .map(b -> TIME_FMT.format(b.getStartTime().toLocalTime()) + " - " +
                            TIME_FMT.format(b.getEndTime().toLocalTime()))
                    .collect(Collectors.toSet());

            List<String> allSlots = generateTimeSlots();

            List<String> availableSlots = allSlots.stream()
                    .filter(slot -> !bookedSlots.contains(slot))
                    .collect(Collectors.toList());

            Map<String, List<String>> slotInfo = new HashMap<>();
            slotInfo.put("available", availableSlots);
            slotInfo.put("booked", new ArrayList<>(bookedSlots));

            response.put(court.getCourtName(), slotInfo);
        }

        return response;
    }


    /** Generate all 1-hour slots (09:00 - 22:00). */
    private List<String> generateTimeSlots() {
        List<String> slots = new ArrayList<>();
        LocalTime start = OPEN_TIME;
        while (start.isBefore(CLOSE_TIME)) {
            LocalTime end = start.plusHours(1);
            slots.add(TIME_FMT.format(start) + " - " + TIME_FMT.format(end));
            start = end;
        }
        return slots;
    }


    public Optional<Booking> findBookingByPayment(Payment payment) {
        if (payment == null || payment.getId() == null) {
            return Optional.empty();
        }
        return bookingRepo.findByPaymentId(payment.getId());
    }

    public Optional<Booking> findBookingById(Long bookingId) {
        return bookingRepo.findById(bookingId);
    }

    public Optional<BookingResponse> getBookingDetails(Long bookingId) {
        return bookingRepo.findBookingDetailsById(bookingId);
    }
}
