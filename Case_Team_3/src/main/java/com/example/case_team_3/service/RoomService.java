package com.example.case_team_3.service;

import com.example.case_team_3.model.Room;
import com.example.case_team_3.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Integer id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));
    }

    public Room createRoom(Room room){
        return roomRepository.save(room);
    }

    public Room updateRoom(Integer roomId, Room updatedRoom) {
        Room room = getRoomById(roomId);
        room.setRoomImg(updatedRoom.getRoomImg());
        room.setRoomType(updatedRoom.getRoomType());
        room.setRoomStatus(updatedRoom.getRoomStatus());
        room.setRoomPrice(updatedRoom.getRoomPrice());
        room.setRoomDescription(updatedRoom.getRoomDescription());
        return roomRepository.save(room);
    }

    public void deleteRoom(Integer roomId) {
        roomRepository.deleteById(roomId);
    }




}
