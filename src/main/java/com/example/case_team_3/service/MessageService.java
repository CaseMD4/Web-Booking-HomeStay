package com.example.case_team_3.service;

import com.example.case_team_3.model.chat.Message;
import com.example.case_team_3.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
  private MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }
   public boolean isFistMessage(String sender) {
        List<Message> messages = messageRepository.findBySender(sender);
        return messages.isEmpty();
   }
   public Message autoReplyMessage(String sender) {
        Message reply =new Message();
        reply.setSender("Bot");
        reply.setContent("Chào bạn tôi có thể giúp gì cho bạn");
        reply.setIsReply(true);
        return messageRepository.save(reply);
   }
}

