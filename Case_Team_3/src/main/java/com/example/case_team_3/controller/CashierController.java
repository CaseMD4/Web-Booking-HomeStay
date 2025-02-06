package com.example.case_team_3.controller;

import com.example.case_team_3.model.Room;
import com.example.case_team_3.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cashier")
public class CashierController {
    @Autowired
    private RoomService roomService;

    @GetMapping("")
    public String viewRooms(@RequestParam(required = false) String status, @RequestParam(required = false) String type, Model model) {
        List<Room> rooms;
        if (status != null) {
            rooms = roomService.getRoomsByStatus(Room.RoomStatus.valueOf(status));
        } else if (type != null) {
            rooms = roomService.getRoomsByType(type);
        } else {
            rooms = roomService.getAllRooms();
        }
        model.addAttribute("rooms", rooms);
        return "cashierHome";
    }

    @PostMapping("/updateRoomStatus")
    public String updateRoomStatus(@RequestParam Long roomId, @RequestParam String status, Model model) {
        Float unpaidAmount = roomService.getUnpaidAmount(roomId);
        if (!roomService.updateRoomStatus(roomId, status)) {
            model.addAttribute("unpaidAmount", unpaidAmount);
            return "payment_pending"; // Chuyển đến trang báo nợ
        }
        return "redirect:/cashier/cashierRoom";
    }
}