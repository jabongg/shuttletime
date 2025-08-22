package com.shuttletime.repository;

import com.shuttletime.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.badmintonCourt.id = :courtId " +
            "AND (:start < b.endTime AND :end > b.startTime)")
    List<Booking> findConflictingBookings(@Param("courtId") Long courtId,
                                          @Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end);
}
