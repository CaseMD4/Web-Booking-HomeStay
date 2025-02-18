package com.example.case_team_3.repository;

import com.example.case_team_3.model.Room;
import com.example.case_team_3.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByRoomStatus(Room.RoomStatus status);
    List<Room> findByRoomType_RoomTypeName(String roomType);
    List<Room> findByRoomDescriptionAndRoomPriceBetweenAndRoomType(String description, Float minPrice, Float maxPrice, RoomType roomType);}
