package com.example.case_team_3.controller;


import com.example.case_team_3.model.Room;
import com.example.case_team_3.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('ROLE_USER')")
public class UserController {

    @Autowired
    private RoomService roomService;

//    customerHome
    @RequestMapping("/userHome")
    public String showCustomerHome() {
        return "customerHome";
    }


    @RequestMapping("/list-rooms")
    public String showUserProfile(Model model) {
        List<Room> rooms = roomService.getAllRooms();
        model.addAttribute("rooms", rooms);
        return "all-rooms-by-customer-t-view";
    }

}
