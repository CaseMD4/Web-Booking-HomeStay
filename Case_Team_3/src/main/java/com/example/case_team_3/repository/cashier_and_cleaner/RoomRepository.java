package com.example.case_team_3.repository.cashier_and_cleaner;

import com.example.case_team_3.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByRoomStatus(Room.RoomStatus roomStatus);
}
