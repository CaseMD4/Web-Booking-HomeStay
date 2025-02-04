package com.example.case_team_3.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomId;

    private String roomImg;

    @ManyToOne
    @JoinColumn(name = "roomType_id")
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;

    private Float roomPrice;

    @Column(columnDefinition = "TEXT")
    private String roomDescription;

    @OneToMany(mappedBy = "room")
    private List<Booking> bookings;

    public enum RoomStatus {
        available, booked, cleaning
    }
}
