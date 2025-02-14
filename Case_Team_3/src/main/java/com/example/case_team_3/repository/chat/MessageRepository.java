package com.example.case_team_3.repository.chat;

import com.example.case_team_3.model.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    boolean existsByChatRoom_Employee_EmployeeIdAndChatRoom_User_UserId(Integer cashierId, Integer userId);
}
