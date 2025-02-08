package com.example.case_team_3.controller;

import com.example.case_team_3.model.Room;
import com.example.case_team_3.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cashier")
public class CashierController {
    @Autowired
    private RoomService roomService;

    @GetMapping("")
    public String viewRooms(@RequestParam(required = false) String status, @RequestParam(required = false) String type, Model model) {
        model.addAttribute("rooms",roomService.getRoomsByStatus(status));
        model.addAttribute("filter", status);
        return "cashierHome";
    }
    @GetMapping("/{id}/payment")
    public String viewPayments(@PathVariable Long id, Model model) {
        model.addAttribute("Room",roomService.getRoomById(id));
        model.addAttribute("price",roomService.getUnpaidAmount(id));
        return "payment_pending";
    }
    @PostMapping("/")
    public String checkOut(@RequestParam("roomId") Long roomId) {
        Room room = roomService.getRoomById(roomId);
        if (room != null) {
            room.setRoomStatus(Room.RoomStatus.cleaning);
            roomService.saveRoom(room);
        }
        return "redirect:/cashier";
    }
}