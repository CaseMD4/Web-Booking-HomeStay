package com.example.case_team_3.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Đây là cột ID duy nhất

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "check_in_date")
    private LocalDateTime bookingCheckInDate;

    @Column(name = "check_out_date")
    private LocalDateTime bookingCheckOutDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus bookingStatus;

    @Column(name = "update_time")
    private LocalDateTime bookingUpdateTime;

    public enum BookingStatus {
        confirmed, completed, canceled
    }
}
