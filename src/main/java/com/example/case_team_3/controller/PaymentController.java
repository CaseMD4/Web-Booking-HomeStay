package com.example.case_team_3.controller;

import com.example.case_team_3.model.Booking;
import com.example.case_team_3.model.Room;
import com.example.case_team_3.model.User;
import com.example.case_team_3.service.IPayService;
import com.example.case_team_3.service.BookingService;
import com.example.case_team_3.service.RoomService;
import com.example.case_team_3.service.UserService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class PaymentController {

    @Autowired
    private IPayService payService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @GetMapping("/rooms/paypal/create-order/{roomId}")
    public String createOrder(@PathVariable("roomId") Integer roomId,
                              @RequestParam("roomPrice") Double roomPrice,
                              @RequestParam("checkInDate") String checkInDateStr,
                              @RequestParam("checkOutDate") String checkOutDateStr,
                              HttpServletRequest request,
                              Authentication authentication) {
        try {
            LocalDateTime checkInDate = LocalDateTime.parse(checkInDateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime checkOutDate = LocalDateTime.parse(checkOutDateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            String currency = "USD";
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String returnUrl = baseUrl + "/payment/paypal-payment-return?success=true&roomId=" + roomId + "&roomPrice=" + roomPrice + "&checkInDate=" + checkInDateStr + "&checkOutDate=" + checkOutDateStr;
            String cancelUrl = baseUrl + "/payment/paypal-payment-return?success=false";

            Payment payment = payService.createPaymentWithPayPal(
                    roomPrice,  // Use the passed roomPrice
                    currency,
                    "paypal",
                    "sale",
                    "Thanh toán cho phòng có ID: " + roomId,
                    cancelUrl,
                    returnUrl);
            for (Links link : payment.getLinks()) {
                if ("approval_url".equalsIgnoreCase(link.getRel())) {
                    return "redirect:" + link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/orderFail";
    }

    @GetMapping("/payment/paypal-payment-return")
    public String paymentReturn(@RequestParam("success") boolean success,
                                @RequestParam(value = "paymentId", required = false) String paymentId,
                                @RequestParam(value = "PayerID", required = false) String payerId,
                                @RequestParam(value = "roomId", required = false) Integer roomId,
                                @RequestParam(value = "roomPrice", required = false) Double roomPrice,
                                @RequestParam(value = "checkInDate", required = false) String checkInDateStr,
                                @RequestParam(value = "checkOutDate", required = false) String checkOutDateStr,
                                Model model, Authentication authentication) {
        if (success) {
            try {
                LocalDateTime checkInDate = LocalDateTime.parse(checkInDateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                LocalDateTime checkOutDate = LocalDateTime.parse(checkOutDateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                Payment paypalPayment = payService.executePayment(paymentId, payerId);

                // Save booking details to the database
                Room room = roomService.getRoomById(roomId);
                User user = userService.findByUserUsername(authentication.getName());

                Booking booking = new Booking();
                booking.setRoom(room);
                booking.setUser(user);
                booking.setBookingCheckInDate(checkInDate);
                booking.setBookingCheckOutDate(checkOutDate);
                booking.setBookingUpdateTime(LocalDateTime.now());

                booking.setBookingStatus(Booking.BookingStatus.confirmed);

                // Lưu booking xuống database
                booking = bookingService.createBooking(booking);

                return "payment-success";
            } catch (PayPalRESTException e) {
                e.printStackTrace();
                return "payment-cancel";
            }
        } else {
            return "payment-cancel";
        }
    }
}
