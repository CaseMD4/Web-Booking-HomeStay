package com.example.case_team_3.controller;

import com.example.case_team_3.model.Booking;
import com.example.case_team_3.model.Room;
import com.example.case_team_3.model.User;
import com.example.case_team_3.service.BookingService;
import com.example.case_team_3.service.RoomService;
import com.example.case_team_3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/rooms")
@SessionAttributes("booking")  // <== Thêm dòng này để lưu đối tượng "booking" trong session
public class UserBookingController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    // Hiển thị danh sách phòng cho người dùng
    @GetMapping
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "rooms";
    }

    // Hiển thị chi tiết phòng
    @GetMapping("/detail/{roomId}")
    public String roomDetail(@PathVariable("roomId") Integer roomId, Model model) {
        Room room = roomService.getRoomById(roomId);
        model.addAttribute("room", room);
        return "room-detail";
    }

    // Hiển thị form đặt phòng
    @GetMapping("/book/{roomId}")
    public String showBookingForm(@PathVariable("roomId") Integer roomId, Model model) {
        Room room = roomService.getRoomById(roomId);
        model.addAttribute("room", room);

        model.addAttribute("booking", new Booking());
        return "booking-form";
    }

    // Xử lý đặt phòng
    @PostMapping("/book/{roomId}")
    public String processBooking(@PathVariable("roomId") Integer roomId,
                                 @ModelAttribute Booking booking,
                                 Authentication authentication) {
        Room room = roomService.getRoomById(roomId);
        booking.setRoom(room);

        User user = userService.findByUserUsername(authentication.getName());
        booking.setUser(user);
        booking.setBookingUpdateTime(LocalDateTime.now());
        booking.setBookingStatus(Booking.BookingStatus.confirmed);

        bookingService.createBooking(booking);
        return "redirect:/rooms?booked=true";
    }
}
