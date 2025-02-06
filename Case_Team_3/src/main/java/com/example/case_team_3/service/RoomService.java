package com.example.case_team_3.service;

import com.example.case_team_3.model.Booking;
import com.example.case_team_3.model.Room;
import com.example.case_team_3.repository.BookingRepository;
import com.example.case_team_3.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RoomService {
    @Autowired
    private  RoomRepository roomRepository;
    private BookingRepository bookingRepository;
        public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    public List<Room> getRoomsByStatus(Room.RoomStatus status) {
        return roomRepository.findByRoomStatus(status);
    }
    public List<Room> getRoomsByType(String roomType) {
        return roomRepository.findByRoomType_RoomTypeName(roomType);
    }
    public Float getUnpaidAmount(Long roomId) {
        List<Booking> unpaidBookings = bookingRepository.findByRoom_RoomIdAndBookingStatus(roomId, Booking.BookingStatus.confirmed);
        float totalAmount = 0f;
        for (Booking booking : unpaidBookings) {
            long totalSeconds = ChronoUnit.SECONDS.between(booking.getBookingCheckInDate(), booking.getBookingCheckOutDate());
            float totalDay = totalSeconds / 86400;
            totalAmount += (booking.getRoom().getRoomPrice()*totalDay);
        }

        return totalAmount;
    }
    public boolean updateRoomStatus(Long roomId, String status) {
        if ("available".equals(status)) {
            Float unpaidAmount = getUnpaidAmount(roomId);
            if (unpaidAmount > 0) {
                return false;
            }
        }
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        room.setRoomStatus(Room.RoomStatus.valueOf(status));
        roomRepository.save(room);
        return true;
    }
}
