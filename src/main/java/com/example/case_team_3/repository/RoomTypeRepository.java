package com.example.case_team_3.repository;

import com.example.case_team_3.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
    RoomType findByRoomTypeName(String roomTypeName);
}