//package com.example.case_team_3.controller;
//
//import com.example.case_team_3.model.Room;
//import com.example.case_team_3.service.PayPalPaymentService;
//import com.example.case_team_3.service.RoomService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import jakarta.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//@Controller
//public class PayPalPaymentController {
//
//    @Autowired
//    private PayPalPaymentService payPalPaymentService;
//
//    @Autowired
//    private RoomService roomService;
//
//    /**
//     * Trang tạo đơn hàng.
//     */
//    @GetMapping({"/", "/createOrder"})
//    public String createOrderPage() {
//        return "createOrder";
//    }
//
//    /**
//     * Khi người dùng nhấn nút "Thanh toán bằng PayPal", tạo đơn hàng và chuyển hướng đến PayPal.
//     */@GetMapping("/paypal/create-order/{roomId}")
//    public String createOrder(@PathVariable("roomId") Integer roomId, HttpServletRequest request) {
//        // Lấy thông tin phòng từ database
//              // cần lưu ý chỗ này
//        Room room = roomService.getRoomById(roomId);
//        // Giả sử roomPrice lưu giá phòng (ví dụ: USD) hoặc bạn cần chuyển đổi nếu cần
//        double amount = room.getRoomPrice();
//        String currency = "USD"; // Hoặc đổi theo yêu cầu
//        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
//        String returnUrl = baseUrl + "/paypal-payment-return?success=true";
//        String cancelUrl = baseUrl + "/paypal-payment-return?success=false";
//
//        try {
//            String approvalLink = payPalPaymentService.createOrder(amount, currency, returnUrl, cancelUrl);
//            return "redirect:" + approvalLink;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "redirect:/orderFail";
//        }
//    }
//
//
//    /**
//     * Callback sau khi người dùng phê duyệt hoặc hủy thanh toán trên PayPal.
//     * Tham số "token" chứa orderId từ PayPal.
//     */
//    @GetMapping("/paypal-payment-return")
//    public String paymentReturn(@RequestParam("token") String orderId,
//                                @RequestParam("success") boolean success,
//                                Model model) {
//        if (success) {
//            try {
//                boolean captureResult = payPalPaymentService.captureOrder(orderId);
//                if (captureResult) {
//                    model.addAttribute("orderId", orderId);
//                    return "orderSuccess";
//                } else {
//                    return "orderFail";
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                return "orderFail";
//            }
//        } else {
//            return "orderFail";
//        }
//    }
//}
