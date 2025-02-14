package com.example.case_team_3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/chat")
public class MessageController {
//    @Autowired
//    private ChatService chatService;
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//    private EmployeeRepository employeeRepository;
//
//    @MessageMapping("/chat.send")
//    public void sendMessage(@RequestParam Integer chatRoomId, @RequestParam String content) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String cashierUsername = authentication.getName();
//        Employee employee = employeeRepository.findByEmployeeUsername(cashierUsername) ;
//        Integer cashierId = employee.getEmployeeId();
//
//        ChatRoom chatRoom = chatService.findChatRoom(chatRoomId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid chatRoomId"));
//
//        if (!chatRoom.getEmployee().getEmployeeId().equals(cashierId)) {
//            throw new IllegalArgumentException("Cashier is not authorized to send messages to this chat room");
//        }
//
//        Integer userId = chatRoom.getUser().getUserId();
//        Message message = new Message();
//        message.setChatRoom(chatRoom);
//
//
//        if (message.getChatRoom().getEmployee().getEmployeeId()==null){
//            messagingTemplate.convertAndSend("/topic/error","chua co cashier nào");
//            return;
//        }
//        if (!chatService.hasCashierMessagedUserBefore(cashierId, userId)){
//            return ;
//        }
//        message.setSender(String.valueOf(cashierId));
//
//        message.setContent(content);
//        message.setTimestamp(LocalDateTime.now());
//
//        messagingTemplate.convertAndSend("/topic/chat." + chatRoom.getId(), message);
//    }
//
//    @GetMapping("/api/chatRoomId")
//    public ResponseEntity<Integer> getChatRoomId(@RequestParam Integer userId) {
//        Optional<ChatRoom> chatRoom = chatService.findChatRoom(userId);
//        return chatRoom.map(value -> new ResponseEntity<>(value.getId(), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    @GetMapping("/api/cashier/chatRooms")
//    public ResponseEntity<List<ChatRoom>> getChatRoomsForCashier(@RequestParam Integer employeeId) {
//        List<ChatRoom> chatRooms = chatService.findChatRoomsForCashier(employeeId);
//        return new ResponseEntity<>(chatRooms, HttpStatus.OK);
//    }
}
