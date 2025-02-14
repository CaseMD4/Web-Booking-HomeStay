package com.example.case_team_3.repository;

import com.example.case_team_3.model.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<com.example.case_team_3.model.chat.Message> findBySender(String sender);
    boolean existsByChatRoom_Employee_EmployeeIdAndChatRoom_User_UserId(Integer cashierId, Integer userId);
}


//
//package com.example.case_team_3.repository;
//
//import com.example.case_team_3.model.Message; // Đảm bảo import đúng lớp Message
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface MessageRepository extends JpaRepository<Message, Long> {
//    List<Message> findBySender(String sender);
//}