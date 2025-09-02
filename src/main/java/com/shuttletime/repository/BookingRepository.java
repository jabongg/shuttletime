package com.shuttletime.repository;

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

}
