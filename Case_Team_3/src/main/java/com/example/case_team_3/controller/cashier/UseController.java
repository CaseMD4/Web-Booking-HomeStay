package com.example.case_team_3.controller.cashier;

import com.example.case_team_3.model.Room;
import com.example.case_team_3.service.cashier_and_cleaner.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('ROLE_USER')")
public class UseController {

    @Autowired
    private RoomService roomService;

    //    customerHome
    @RequestMapping("/customerHome")
    public String showCustomerHome() {
        return "cashier/customerHome";
    }


    @RequestMapping("/list-rooms")
    public String showUserProfile(Model model) {
        List<Room> rooms = roomService.findAll();
        model.addAttribute("rooms", rooms);
        return "/cashier/all-room-by-customer-t-view";
    }
}
