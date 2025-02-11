package com.example.case_team_3.controller.cashier;

import com.example.case_team_3.model.Room;
import com.example.case_team_3.model.cashier.Invoice;
import com.example.case_team_3.model.cashier.TransactionHistory;
import com.example.case_team_3.service.InvoiceService;
import com.example.case_team_3.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/cashier")
public class CashierController {
    @Autowired
    private RoomService roomService;
@Autowired
private InvoiceService invoiceService;
    @GetMapping("")
    public String viewRooms(@RequestParam(required = false) String status, Model model) {
        model.addAttribute("rooms",roomService.getRoomsByStatus(status));
        model.addAttribute("filter", status);
        return "/cashier/cashierHome";
    }
    @GetMapping("/{id}/payment")
    public String viewPayments(@PathVariable Integer id, Model model) {
        model.addAttribute("room",roomService.getRoomById(id));
        model.addAttribute("price",roomService.getUnpaidAmount(id));
        return "/cashier/payment_pending";
    }
    @PostMapping("/{id}/payment")
    public String processPayment(@PathVariable Integer id,
                                 @RequestParam("amountPaid") Float amountPaid,
                                 Model model) {
        float unpaidAmount = roomService.getUnpaidAmount(id);

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

        roomService.processPayment(id, amountPaid);

        Room room = roomService.getRoomById(id);
        if (room != null) {
            room.setRoomStatus(Room.RoomStatus.cleaning);
            roomService.saveRoom(room);
        }

        return "redirect:/cashier";
    }

    @PostMapping("/checkout")
    public String checkOut(@RequestParam("roomId") Integer roomId) {
        float unpaidAmount = roomService.getUnpaidAmount(roomId);
        if (unpaidAmount >0) {
            return "redirect:/cashier/" + roomId + "/cashier/payment_pending";
        }

        Room room = roomService.getRoomById(roomId);
        if (room != null) {
            room.setRoomStatus(Room.RoomStatus.cleaning);
            roomService.saveRoom(room);
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
        float totalAmount = roomService.getUnpaidAmount(id);
        String customerName = roomService.getNameCustomerBooking(id);
        if (customerName == null || customerName.isEmpty()) {
            customerName = "Khách vãng lai" ;
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
}