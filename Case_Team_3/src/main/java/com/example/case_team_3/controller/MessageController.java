package com.example.case_team_3.controller;
import com.example.case_team_3.model.Employee;
import com.example.case_team_3.model.User;
import com.example.case_team_3.model.chat.ChatRoom;
import com.example.case_team_3.model.chat.Message;
import com.example.case_team_3.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/chat")
public class MessageController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload Message message) {

        String userName = message.getSender();
        User userSender = chatService.findUserByUsername(userName);

        if (userSender == null) {
            System.out.println("User not found with username: " + userName);
            return;
        }

        Integer userId = userSender.getUserId();
        System.out.println("Received message from user: " + userId);
        System.out.println("Message content: " + message.getContent());
        System.out.println("Message senderType: " + message.getSenderType());

        ChatRoom chatRoom = chatService.getOrCreateChatRoom(userId);

        if (chatRoom.getEmployee() == null) {
            chatService.assignChatRoomToRandomCashier(chatRoom.getId());
        }

        Employee employee = chatRoom.getEmployee();
        String receiver = null;
        Message.SenderType senderType = message.getSenderType();

        if (senderType == Message.SenderType.user) {
            if (employee != null) {
                receiver = employee.getEmployeeUsername();
            } else {
                System.out.println("Không có thu ngân nào được gán cho chatRoom này.");
                return;
            }
        } else {
            receiver = userSender.getUserUsername();
        }

        if (receiver == null) {
            System.out.println("Không có người nhận tin nhắn.");
            return;
        }

        System.out.println("Sending message to: " + receiver);
        chatService.saveMessage(chatRoom.getId(), String.valueOf(userId), receiver, message.getContent(), senderType);
        messagingTemplate.convertAndSend("/topic/chat." + receiver, message);

        // Gửi message đến user
        messagingTemplate.convertAndSend("/topic/chat." + userId, message);

        // Thông báo cho thu ngân nếu có
        if (employee != null) {
            System.out.println("Sending chatRoomId " + chatRoom.getId() + " to cashier " + employee.getEmployeeUsername());
            Map<String, Object> payload = new HashMap<>();
            payload.put("chatRoomId", chatRoom.getId());
            payload.put("userId", userId);
            payload.put("userName", userName);
            System.out.println("Sending payload " + payload + " to cashier " + employee.getEmployeeUsername());// Log payload
            messagingTemplate.convertAndSend("/topic/cashier." + employee.getEmployeeUsername(), payload);
        } else {
            System.out.println("Không có thu ngân nào được gán cho chatRoom này.");
        }
    }
}
