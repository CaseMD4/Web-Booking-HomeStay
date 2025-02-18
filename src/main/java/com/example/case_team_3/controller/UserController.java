package com.example.case_team_3.controller;


import com.example.case_team_3.model.Booking;
import com.example.case_team_3.model.Room;
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
@RequestMapping("/user")
@PreAuthorize("hasRole('ROLE_USER')")
public class UserController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

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


//    @todo : từ dưới là của anh

    //     phương thức tìm phòng
    @GetMapping("/detail/{roomId}")
    public String roomDetail(@PathVariable("roomId") Integer roomId, Model model) {
        Room room = roomService.getRoomById(roomId);
        model.addAttribute("room", room);
        return "room-details-by-customer";
    }


    @GetMapping("/book/{roomId}")
    public String showBookingForm(@PathVariable("roomId") Integer roomId, Model model) {
        Room room = roomService.getRoomById(roomId);
        model.addAttribute("room", room);
        Booking booking = new Booking();
        booking.setBookingCheckInDate(LocalDateTime.now()); // Set default values if needed
        booking.setBookingCheckOutDate(LocalDateTime.now().plusDays(1));
        model.addAttribute("booking", booking);
        return "booking-form"; // Return to the booking form
    }

    @PostMapping("/book/{roomId}")
    public String processBooking(@PathVariable("roomId") Integer roomId,
                                 @ModelAttribute Booking booking,
                                 Authentication authentication) {
        Room room = roomService.getRoomById(roomId);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String checkInDate = booking.getBookingCheckInDate().format(formatter);
        String checkOutDate = booking.getBookingCheckOutDate().format(formatter);

        return "redirect:/payment/rooms/paypal/create-order/" + roomId + "?roomPrice=" + room.getRoomPrice() + "&checkInDate=" + checkInDate + "&checkOutDate=" + checkOutDate;
    }

    @GetMapping("/bookings")
    public String viewBookingHistory(Model model, Authentication authentication) {
        // 1. Get the currently logged-in user
        String username = authentication.getName();
        User user = userService.findByUserUsername(username);

        // 2. Retrieve the booking history for the user
        List<Booking> bookings = bookingService.getBookingsByUser(user);

        // 3. Add the bookings to the model
        model.addAttribute("bookings", bookings);

        // 4. Return the name of the Thymeleaf template
        return "booking-history";
    }
}
