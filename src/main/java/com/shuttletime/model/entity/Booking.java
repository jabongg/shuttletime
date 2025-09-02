package com.shuttletime.model.entity;

import com.shuttletime.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"court_id", "startTime", "endTime"})
)
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    //@JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // not using for now : thought to use it in search api
//    private LocalDate bookingDate;   // ✅ must exist if query uses it

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id")
    private BadmintonCourt badmintonCourt;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.CONFIRMED;

    private LocalDateTime bookingTime;

    @OneToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;

    public Booking() {
        this.bookingTime = LocalDateTime.now();
    }

}
