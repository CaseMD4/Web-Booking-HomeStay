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
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime bookingCheckInDate;
    private LocalDateTime bookingCheckOutDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    private LocalDateTime bookingUpdateTime;
    public enum BookingStatus {
        confirmed, completed, canceled
    }
}
