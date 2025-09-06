package com.shuttletime.repository;

import com.shuttletime.model.dto.BookingResponse;
import com.shuttletime.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.badmintonCourt.id = :courtId " +
            "AND (:start < b.endTime AND :end > b.startTime)")
    List<Booking> findConflictingBookings(@Param("courtId") Long courtId,
                                          @Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end);


//    @Query("SELECT b FROM Booking b WHERE b.badmintonCourt.venue.id = :venueId AND b.bookingDate = :date")
//    List<Booking> findByVenueIdAndDate(@Param("venueId") Long venueId,
//                                       @Param("date") LocalDate date);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.badmintonCourt.venue.id = :venueId " +
            "AND b.startTime BETWEEN :startOfDay AND :endOfDay")
    List<Booking> findBookingsByVenueAndDate(@Param("venueId") Long venueId,
                                             @Param("startOfDay") LocalDateTime startOfDay,
                                             @Param("endOfDay") LocalDateTime endOfDay);

    // Fetch bookings for a given court within a time range
    List<Booking> findByBadmintonCourt_IdAndStartTimeBetween(Long courtId,
                                                   LocalDateTime start,
                                                   LocalDateTime end);

    // Custom query method - Spring Data will generate the implementation
    Optional<Booking> findByPaymentId(Long paymentId);



//    @Query("""
//        SELECT new com.example.dto.BookingResponse(
//            b.id,
//            u.username,
//            v.name,
//            c.courtName,
//            FUNCTION('to_char', b.bookingTime, 'YYYY-MM-DD'),
//            CONCAT(FUNCTION('to_char', b.startTime, 'HH12:MI AM'), ' - ', FUNCTION('to_char', b.endTime, 'HH12:MI AM')),
//            p.amount
//        )
//        FROM Booking b
//        JOIN b.user u
//        JOIN b.court c
//        JOIN c.venue v
//        JOIN b.payment p
//        WHERE b.id = :bookingId
//    """)

    @Query(value = """
    SELECT 
        b.id AS booking_id,
        u.username AS user_name,
        v.name AS venue_name,
        c.court_name AS court_name,
        TO_CHAR(b.booking_time, 'YYYY-MM-DD') AS booking_date,
        TO_CHAR(b.start_time, 'HH12:MI AM') || ' - ' || TO_CHAR(b.end_time, 'HH12:MI AM') AS slot_time,
        p.amount AS amount
    FROM booking b
    JOIN users u ON b.user_user_id = u.user_id
    JOIN badminton_court c ON b.court_id = c.id
    JOIN venue v ON c.venue_id = v.id
    JOIN payments p ON b.payment_id = p.id
    WHERE b.id = :bookingId
    """, nativeQuery = true)
    Optional<BookingResponse> findBookingDetailsById(@Param("bookingId") Long bookingId);


}
