package com.example.case_team_3.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "room_type")
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomTypeId;

    private String roomTypeName;

    @OneToMany(mappedBy = "roomType")
    private List<Room> rooms;
}
