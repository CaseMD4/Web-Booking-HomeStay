package com.example.case_team_3.controller;


import com.example.case_team_3.model.Room;
import com.example.case_team_3.model.User;
import com.example.case_team_3.model.chat.ChatRoom;
import com.example.case_team_3.model.chat.Message;
import com.example.case_team_3.repository.UserRepository;
import com.example.case_team_3.service.RoomService;
import com.example.case_team_3.service.UserService;
import com.example.case_team_3.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('ROLE_USER')")
public class UserController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserRepository userRepository;

//    customerHome
    @RequestMapping("")
    public String showCustomerHome(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
        return "customerHome";
    }


    @RequestMapping("/list-rooms")
    public String showUserProfile(Model model) {
        List<Room> rooms = roomService.getAllRooms();
        model.addAttribute("rooms", rooms);
        return "all-rooms-by-customer-t-view";
    }
    @PostMapping("/send-message")
    public ResponseEntity<?> sendMessage(@RequestParam("userId") Integer userId,
                                         @RequestParam("content") String content) {

        ChatRoom chatRoom = chatService.getOrCreateChatRoom(userId); // Lấy hiện có hoặc tạo mới


        if(chatRoom.getEmployee() == null) {
            chatService.assignChatRoomToRandomCashier(chatRoom.getId());
        }


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + userId));

        chatService.saveMessage(chatRoom.getId(), String.valueOf(userId), chatRoom.getEmployee().getEmployeeId().toString(), content, Message.SenderType.user);

        return ResponseEntity.ok().build();
    }

}
