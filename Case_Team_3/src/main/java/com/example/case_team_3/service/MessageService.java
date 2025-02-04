package com.example.case_team_3.service;

import com.example.case_team_3.model.Message;
import com.example.case_team_3.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    @Autowired
  private   MessageRepository messageRepository;

    public Message saveMessage(String sender, String receiver, String content) {
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }
    public List<Message> getChatHistory(String user1, String user2) {
        return messageRepository.findAllByOrderByIdDesc(user1, user2);
    }
}
