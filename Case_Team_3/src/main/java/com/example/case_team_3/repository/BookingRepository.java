package com.example.case_team_3.repository;

import com.example.case_team_3.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BookingRepository {
    List<Booking> findByRoom_RoomIdAndBookingStatus(Long roomId, Booking.BookingStatus bookingStatus);
}
