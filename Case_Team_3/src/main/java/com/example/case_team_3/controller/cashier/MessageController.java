package com.example.case_team_3.controller.cashier;

import com.example.case_team_3.model.Message;
import com.example.case_team_3.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/chat")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public Message sendMessage(Message message) {
        Message savedMessage =messageService.saveMessage(message);
        if (messageService.isFistMessage(message.getSender())){
            Message autoReyply =messageService.autoReplyMessage(message.getSender());
            return autoReyply;
        }
        return savedMessage;
    }
}
