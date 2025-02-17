package com.example.case_team_3.repository.chat;

import com.example.case_team_3.model.chat.ChatRoom;
import com.example.case_team_3.model.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<com.example.case_team_3.model.chat.Message> findBySender(String sender);
    List<Message> findByChatRoom(ChatRoom chatRoom);
    List<Message> findByChatRoomOrderByTimestampAsc(ChatRoom chatRoom);
}

