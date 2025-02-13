package com.example.case_team_3.controller.cashier;

import com.example.case_team_3.model.Message;
import com.example.case_team_3.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // Dùng để gửi WebSocket message

    @PostMapping("/chat/send")
    public void sendMessage(@RequestParam String sender, @RequestParam String content) {

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver("Cashier");
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setIsReply(false);

        messageService.saveMessage(message);
        messagingTemplate.convertAndSend("/topic/messages", message);
    }
}
