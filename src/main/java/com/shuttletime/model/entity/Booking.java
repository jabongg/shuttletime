package com.shuttletime.model.entity;

import com.shuttletime.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"court_id", "startTime", "endTime"})
)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    //@JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id")
    private BadmintonCourt badmintonCourt;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;
}
