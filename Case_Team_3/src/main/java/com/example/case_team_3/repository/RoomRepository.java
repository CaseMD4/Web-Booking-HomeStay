package com.example.case_team_3.repository;

import com.example.case_team_3.model.Room;
import com.example.case_team_3.model.Room.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    // Tìm danh sách phòng theo trạng thái (ví dụ: available, booked, cleaning)
    List<Room> findByRoomStatus(RoomStatus roomStatus);

    // Nếu cần tìm phòng theo kiểu phòng
    List<Room> findByRoomType_RoomTypeName(String roomTypeName);
}
