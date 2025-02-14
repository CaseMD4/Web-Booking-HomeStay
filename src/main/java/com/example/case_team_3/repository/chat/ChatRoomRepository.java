package com.example.case_team_3.repository.chat;

import com.example.case_team_3.model.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    List<ChatRoom> findByChatRoomStatus(ChatRoom.ChatRoomStatus status);
    Optional<ChatRoom> findByUser_UserId(Integer userId);
    List<ChatRoom> findByEmployee_EmployeeId(Integer employeeId);
}
