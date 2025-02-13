//package com.example.case_team_3.controller;
//
//import com.example.case_team_3.model.Booking;
//import com.example.case_team_3.model.Booking.BookingStatus;
//import com.example.case_team_3.model.Room;
//import com.example.case_team_3.model.User;
//import com.example.case_team_3.service.BookingService;
//import com.example.case_team_3.service.PaymentService;
//import com.example.case_team_3.service.RoomService;
//import com.example.case_team_3.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.support.SessionStatus;
//import org.springframework.security.core.Authentication;
//
//import java.time.LocalDateTime;
//
//@Controller
//@RequestMapping("/payment")
//@SessionAttributes("booking")
//public class PaymentController {
//
//    @Autowired
//    private PaymentService paymentService;
//
//    @Autowired
//    private RoomService roomService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private BookingService bookingService;
//
//    /**
//     * Nhận thông tin booking từ form đặt phòng và lưu tạm trong session.
//     * Sau đó hiển thị trang chọn phương thức thanh toán.
//     */
//    @PostMapping("/process/{roomId}")
//    public String processBooking(@PathVariable("roomId") Integer roomId,
//                                 @ModelAttribute Booking booking,
//                                 Authentication authentication,
//                                 Model model) {
//        // Lấy thông tin phòng và người dùng
//        Room room = roomService.getRoomById(roomId);
//        User user = userService.findByUserUsername(authentication.getName());
//
//        // Thiết lập thông tin booking
//        booking.setRoom(room);
//        booking.setUser(user);
//        booking.setBookingUpdateTime(LocalDateTime.now());
//        booking.setBookingStatus(BookingStatus.pending);
//
//        // Lưu booking vào model (và session nhờ @SessionAttributes)
//        model.addAttribute("booking", booking);
//        // Hiển thị trang chọn phương thức thanh toán
//        return "payment-method-selection"; // view để người dùng chọn phương thức thanh toán
//    }
//
//    /**
//     * Xử lý khi người dùng chọn phương thức thanh toán và nhấn “Thanh toán”.
//     * Hệ thống sẽ gọi PaymentService để tạo yêu cầu thanh toán và redirect.
//     */
//    @PostMapping("/execute")
//    public String executePayment(@RequestParam("paymentMethod") String paymentMethod,
//                                 @ModelAttribute("booking") Booking booking) {
//        // Tạo yêu cầu thanh toán (giả lập)
//        String redirectUrl = paymentService.initiatePayment(booking, paymentMethod);
//        return "redirect:" + redirectUrl;
//    }
//
//    /**
//     * Callback từ VNPay (giả lập).
//     * Tham số paymentStatus được giả lập để chỉ ra thành công hay thất bại.
//     */
//    @GetMapping("/return/vnpay")
//    public String vnpayReturn(@RequestParam("paymentStatus") String paymentStatus,
//                              @ModelAttribute("booking") Booking booking,
//                              SessionStatus sessionStatus) {
//        if ("success".equalsIgnoreCase(paymentStatus)) {
//            booking.setBookingStatus(BookingStatus.confirmed);
//            bookingService.createBooking(booking);
//            paymentService.notifyAdmin(booking, "VNPay");
//            sessionStatus.setComplete();
//            return "payment-success"; // view thông báo thanh toán thành công
//        } else {
//            sessionStatus.setComplete();
//            return "payment-failure"; // view thông báo thanh toán thất bại
//        }
//    }
//
//    /**
//     * Callback từ PayPal (giả lập).
//     */
//    @GetMapping("/return/paypal")
//    public String paypalReturn(@RequestParam("paymentStatus") String paymentStatus,
//                               @ModelAttribute("booking") Booking booking,
//                               SessionStatus sessionStatus) {
//        if ("success".equalsIgnoreCase(paymentStatus)) {
//            booking.setBookingStatus(BookingStatus.confirmed);
//            bookingService.createBooking(booking);
//            paymentService.notifyAdmin(booking, "PayPal");
//            sessionStatus.setComplete();
//            return "payment-success";
//        } else {
//            sessionStatus.setComplete();
//            return "payment-failure";
//        }
//    }
//}
