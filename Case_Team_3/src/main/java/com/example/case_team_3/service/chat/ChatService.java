package com.example.case_team_3.service.chat;

import com.example.case_team_3.model.Employee;
import com.example.case_team_3.model.User;
import com.example.case_team_3.model.chat.ChatRoom;
import com.example.case_team_3.model.chat.Message;
import com.example.case_team_3.repository.EmployeeRepository;
import com.example.case_team_3.repository.chat.MessageRepository;
import com.example.case_team_3.repository.UserRepository;
import com.example.case_team_3.repository.chat.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    // Sửa đổi createChatRoom chỉ được gọi khi Người dùng thực sự gửi tin nhắn
    public ChatRoom getOrCreateChatRoom(Integer userId) {
        // Tìm phòng chat hiện có cho người dùng nếu tồn tại;
        ChatRoom chatRoom = chatRoomRepository.findByUser_UserId(userId).orElse(null);

        if (chatRoom == null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + userId));

            chatRoom = new ChatRoom();
            chatRoom.setUser(user);
            chatRoom.setChatRoomStatus(ChatRoom.ChatRoomStatus.pending); // Ban đầu là chờ xử lý
            chatRoom = chatRoomRepository.save(chatRoom);
        }
        return chatRoom;
    }

    public Employee getRandomAvailableCashier() {
        List<Employee> cashiers = employeeRepository.findByEmployeeRole(Employee.EmployeeRole.cashier);
        if (cashiers.isEmpty()) {
            return null; // Không có thu ngân nào
        }
        Random random = new Random();
        int randomIndex = random.nextInt(cashiers.size());
        return cashiers.get(randomIndex);
    }

    public ChatRoom assignChatRoomToRandomCashier(Integer chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng chat với ID: " + chatRoomId));

        // Kiểm tra xem chatRoom đã được gán cho thu ngân hay chưa
        if (chatRoom.getEmployee() != null) {
            return chatRoom;  // đã được gán
        }

        Employee cashier = getRandomAvailableCashier();
        if (cashier != null) {
            chatRoom.setEmployee(cashier);
            chatRoom.setChatRoomStatus(ChatRoom.ChatRoomStatus.active);
            return chatRoomRepository.save(chatRoom);
        } else {
            throw new IllegalStateException("Không có thu ngân nào để chỉ định.");
        }
    }

    public Optional<ChatRoom> findChatRoomByUser(Integer userId) {
        return chatRoomRepository.findByUser_UserId(userId);
    }

    public List<ChatRoom> findChatRoomsForCashier(Integer employeeId) {
        return chatRoomRepository.findByEmployee_EmployeeId(employeeId);
    }

    public Message saveMessage(Integer chatRoomId, String sender, String receiver, String content, Message.SenderType senderType) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng chat với ID: " + chatRoomId));

        Message message = new Message();
        message.setChatRoom(chatRoom);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setSenderType(senderType);
        message.setIsReply(false);
        return messageRepository.save(message);
    }
    public User findUserByUsername(String userName) {
        return userRepository.findByUserUsername(userName);
    }
    public List<Message> getMessagesForChatRoom(ChatRoom chatRoom) {
        return messageRepository.findByChatRoomOrderByTimestampAsc(chatRoom); // Sắp xếp theo thời gian
    }
}
