package com.example.case_team_3.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "image_room_detail")
public class ImageRoomDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "image_room_detail_place_count", nullable = false)
    private int imageRoomDetailPlaceCount;

    @Column(name = "image_room_detail_link", nullable = false)
    private String imageRoomDetailLink;
}
