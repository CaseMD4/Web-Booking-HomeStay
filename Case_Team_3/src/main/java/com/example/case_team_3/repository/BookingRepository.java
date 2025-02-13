package com.example.case_team_3.repository;

import com.example.case_team_3.model.Booking;
import com.example.case_team_3.model.Room;
import com.example.case_team_3.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository<Interger> extends JpaRepository<Booking, Interger> {

    // Tìm danh sách booking của một user cụ thể
    List<Booking> findByUser(User user);

    // Tìm danh sách booking của một phòng cụ thể
    List<Booking> findByRoom(Room room);
}
