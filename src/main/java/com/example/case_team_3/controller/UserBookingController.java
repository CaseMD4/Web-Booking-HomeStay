package com.example.case_team_3.controller;

import com.example.case_team_3.model.Booking;
import com.example.case_team_3.model.Room;
import com.example.case_team_3.model.RoomType;
import com.example.case_team_3.model.User;
import com.example.case_team_3.service.BookingService;
import com.example.case_team_3.service.RoomService;
import com.example.case_team_3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/rooms")
@PreAuthorize("hasRole('ROLE_USER')")
public class UserBookingController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;


    @GetMapping
    public String listAndSearchRooms(
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Float priceMin,
            @RequestParam(required = false) Float priceMax,
            @RequestParam(required = false) String roomType,
            Model model) {

        List<Room> rooms;
        if (description != null || priceMin != null || priceMax != null || roomType != null) {
            RoomType roomTypeO = roomService.getRoomTypeByRoomTypeName(roomType);

            rooms = roomService.searchRooms( description, priceMin, priceMax, roomTypeO);
        } else {
            rooms = roomService.getAllRooms();
        }
        model.addAttribute("rooms", rooms);
        return "customerHome";
    }

    @GetMapping("/detail/{roomId}")
    public String roomDetail(@PathVariable("roomId") Integer roomId, Model model) {
        Room room = roomService.getRoomById(roomId);
        model.addAttribute("room", room);
        return "room-detail";
    }


}
