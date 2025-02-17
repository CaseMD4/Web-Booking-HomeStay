package com.example.case_team_3.controller;

import com.example.case_team_3.model.Employee;
import com.example.case_team_3.model.Room;
import com.example.case_team_3.model.cashier.Invoice;
import com.example.case_team_3.model.cashier.TransactionHistory;
import com.example.case_team_3.model.chat.ChatRoom;
import com.example.case_team_3.model.chat.Message;
import com.example.case_team_3.repository.EmployeeRepository;
import com.example.case_team_3.service.RoomService;
import com.example.case_team_3.service.cashier_and_cleaner.InvoiceService;
import com.example.case_team_3.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/cashier")
@PreAuthorize("hasRole('ROLE_CASHIER')")
public class CashierController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private ChatService chatService;

    @Autowired
    private EmployeeRepository employeeRepository;




@GetMapping("")
public String viewRooms(@RequestParam(required = false) String status, Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String cashierUsername = authentication.getName();
    Employee employee = employeeRepository.findByEmployeeUsername(cashierUsername);

   List<ChatRoom> chatRooms = chatService.findChatRoomsForCashier(employee.getEmployeeId());

   model.addAttribute("chatRooms", chatRooms);
    model.addAttribute("cashierUsername", cashierUsername);
    model.addAttribute("rooms", roomService.getRoomsByStatus(status));
    model.addAttribute("filter", status);
    return "cashier/cashierPage";
}

    @GetMapping("/{id}/payment")
    public String viewPayments(@PathVariable Integer id, Model model) {
        model.addAttribute("room", roomService.getRoomById(id));
        model.addAttribute("price", roomService.getUnpaidAmount(Long.valueOf(id)));
        return "/cashier/payment_pending";
    }

    @PostMapping("/{id}/payment")
    public String processPayment(@PathVariable Integer id,
                                 @RequestParam("amountPaid") Float amountPaid,
                                 Model model) {
        float unpaidAmount = roomService.getUnpaidAmount(Long.valueOf(id));

        if (amountPaid == null || amountPaid <= 0) {
            model.addAttribute("error", "Số tiền thanh toán không hợp lệ!");
            model.addAttribute("room", roomService.getRoomById(id));
            model.addAttribute("price", unpaidAmount);
            return "/cashier/payment_pending";
        }

        if (amountPaid < unpaidAmount) {
            model.addAttribute("error", "Số tiền thanh toán chưa đủ! Vui lòng nhập số tiền còn thiếu.");
            model.addAttribute("room", roomService.getRoomById(id));
            model.addAttribute("price", unpaidAmount);
            return "/cashier/payment_pending";
        }

        roomService.processPayment(Long.valueOf(id), amountPaid);

        Room room = roomService.getRoomById(id);
        if (room != null) {
            room.setRoomStatus(Room.RoomStatus.cleaning);
            roomService.createRoom(room);
        }

        return "redirect:/cashier";
    }

    @PostMapping("/checkout")
    public String checkOut(@RequestParam("roomId") Integer roomId) {
        float unpaidAmount = roomService.getUnpaidAmount(Long.valueOf(roomId));
        if (unpaidAmount > 0) {
            return "redirect:/cashier/" + roomId + "/cashier/payment_pending";
        }

        Room room = roomService.getRoomById(roomId);
        if (room != null) {
            room.setRoomStatus(Room.RoomStatus.cleaning);
            roomService.createRoom(room);
        }
        return "redirect:/cashier";
    }

    @GetMapping("/{id}/transactions")
    public String viewTransactionHistory(@PathVariable Integer id, Model model) {
        List<TransactionHistory> transactions = roomService.fetchTransactionHistory(id);
        model.addAttribute("transactions", transactions);
        model.addAttribute("room", roomService.getRoomById(id));
        return "/cashier/transaction_history";
    }

    @GetMapping("/{id}/invoice")
    public ResponseEntity<byte[]> generateInvoice(@PathVariable Integer id) {
        Random random = new Random();
        int idInvoice = random.nextInt(10000);
        Room room = roomService.getRoomById(id);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }
        List<TransactionHistory> transactions = roomService.fetchTransactionHistory(id);
        float totalAmount = roomService.getUnpaidAmount(Long.valueOf(id));
        String customerName = roomService.getNameCustomerBooking(id);
        if (customerName == null || customerName.isEmpty()) {
            customerName = "Khách vãng lai";
        }

        Invoice invoice = new Invoice(
                idInvoice,
                customerName,
                id,
                LocalDate.now(),
                transactions,
                totalAmount
        );

        byte[] pdfBytes = invoiceService.generateInvoice(invoice);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
    @PostMapping("/send-message")
    public ResponseEntity<?> sendMessage(@RequestParam("chatRoomId") Integer chatRoomId,
                                         @RequestParam("content") String content,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Employee employee = employeeRepository.findByEmployeeUsername(username);

        chatService.saveMessage(chatRoomId, String.valueOf(employee.getEmployeeId()), null, content, Message.SenderType.cashier);

        return ResponseEntity.ok().build();
    }
}



//    /  @todo cũ
//
//    @GetMapping("")
//    public String viewRooms(@RequestParam(required = false) String status, @RequestParam(required = false) String type, Model model) {
//        List<Room> rooms;
//        if (status != null) {
//            rooms = roomService.getRoomsByStatus(Room.RoomStatus.valueOf(status));
//        } else if (type != null) {
//            rooms = roomService.getRoomsByType(type);
//        } else {
//            rooms = roomService.getAllRooms();
//        }
//        model.addAttribute("rooms", rooms);
//        return "cashierHome";
//    }

//    @PostMapping("/updateRoomStatus")
//    public String updateRoomStatus(@RequestParam Long roomId, @RequestParam String status, Model model) {
//        Float unpaidAmount = roomService.getUnpaidAmount(roomId);
//        if (!roomService.updateRoomStatus(roomId, status)) {
//            model.addAttribute("unpaidAmount", unpaidAmount);
//            return "payment_pending"; // Chuyển đến trang báo nợ
//        }
//        return "redirect:/cashier/cashierRoom";
//    }





