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
    @Autowired
    private BookingRepository bookingRepository;
    public List<Room> getRoomsByStatus(String status) {

        if (status == null || status.isEmpty()) {
            return roomRepository.findAll();
        }
        try {
            Room.RoomStatus statusEnum = Room.RoomStatus.valueOf(status);
            return roomRepository.findByRoomStatus(statusEnum);
        } catch (IllegalArgumentException e) {
            return roomRepository.findAll();
        }
    }
    public void saveRoom(Room room) {
        roomRepository.save(room);
    }
    public  Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId).orElse(null);
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
    public boolean markRoomAsCleaned(Long roomId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room != null && room.getRoomStatus() == Room.RoomStatus.cleaning) {
            room.setRoomStatus(Room.RoomStatus.available);
            roomRepository.save(room);
            return true;
        }
        return false;
    }
}
