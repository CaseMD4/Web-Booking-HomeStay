package com.example.case_team_3.service.chat;

import com.example.case_team_3.model.Employee;
import com.example.case_team_3.model.User;
import com.example.case_team_3.model.chat.ChatRoom;
import com.example.case_team_3.model.chat.Message;
import com.example.case_team_3.repository.EmployeeRepository;
import com.example.case_team_3.repository.MessageRepository;
import com.example.case_team_3.repository.UserRepository;
import com.example.case_team_3.repository.chat.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class ChatService {
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;

    public ChatRoom getOrCreateChatRoom(Integer userId) {
        return chatRoomRepository.findByUser_UserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));

                    ChatRoom chatRoom = new ChatRoom();
                    chatRoom.setUser(user);
                    chatRoom.setChatRoomStatus(ChatRoom.ChatRoomStatus.pending);
                    return chatRoomRepository.save(chatRoom);
                });
    }

    public Employee getRandomCashier() {
        List<Employee> cashiers = employeeRepository.findByEmployeeRole(Employee.EmployeeRole.cashier);
        return cashiers.isEmpty() ? null : cashiers.get(new Random().nextInt(cashiers.size()));
    }

    public Employee assignChatRoomToCashier(ChatRoom chatRoom) {
        Employee cashier = getRandomCashier();
        if (chatRoom != null) {
            chatRoom.setEmployee(cashier);
            chatRoom.setChatRoomStatus(ChatRoom.ChatRoomStatus.active);
            chatRoomRepository.save(chatRoom);
        }
        return cashier;
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public Optional<ChatRoom> findChatRoom(Integer userId) {
        return chatRoomRepository.findByUser_UserId(userId);
    }

    public List<ChatRoom> findPendingChatRooms() {
        return chatRoomRepository.findByChatRoomStatus(ChatRoom.ChatRoomStatus.pending);
    }

    public List<ChatRoom> findChatRoomsForCashier(Integer employeeId) {
        return chatRoomRepository.findByEmployee_EmployeeId(employeeId);
    }
    public boolean hasCashierMessagedUserBefore(Integer cashierId, Integer userId) {
        return messageRepository.existsByChatRoom_Employee_EmployeeIdAndChatRoom_User_UserId(cashierId, userId);
    }
}
