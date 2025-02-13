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
    MessageRepository messageRepository;
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public boolean isFirstMessage(String sender) {
        return messageRepository.findBySender(sender).isEmpty();
    }

    public Message autoReplyMessage(String sender) {
        Message reply = new Message();
        reply.setSender("Bot");
        reply.setReceiver(sender);
        reply.setContent("Chào bạn! Tôi có thể giúp gì cho bạn?");
        reply.setTimestamp(LocalDateTime.now());
        reply.setIsReply(true);
        return messageRepository.save(reply);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

}
