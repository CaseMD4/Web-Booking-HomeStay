//package com.example.case_team_3.service;
//
//import com.example.case_team_3.model.Booking;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PaymentService {
//
//    /**
//     * Giả lập tạo yêu cầu thanh toán.
//     * Trong thực tế, bạn cần gọi API của VNPay/PayPal để tạo yêu cầu thanh toán và lấy URL thanh toán.
//     * @param booking    thông tin booking
//     * @param paymentMethod "vnpay" hoặc "paypal"
//     * @return URL để redirect đến (giả lập)
//     */
//    public String initiatePayment(Booking booking, String paymentMethod) {
//        // Giả lập: nếu chọn VNPay, redirect về /payment/return/vnpay?paymentStatus=success
//        if ("vnpay".equalsIgnoreCase(paymentMethod)) {
//            return "/payment/return/vnpay?paymentStatus=success";
//        } else if ("paypal".equalsIgnoreCase(paymentMethod)) {
//            return "/payment/return/paypal?paymentStatus=success";
//        } else {
//            // Nếu không xác định, giả lập thanh toán thất bại
//            return "/payment/return/vnpay?paymentStatus=failure";
//        }
//    }
//
//    /**
//     * Giả lập gửi thông báo cho admin.
//     * Trong thực tế, bạn có thể gửi email hoặc tích hợp hệ thống thông báo.
//     */
//    public void notifyAdmin(Booking booking, String paymentMethod) {
//        System.out.println("Admin Notification: New booking confirmed via " + paymentMethod
//                + ". Booking details: " + booking.toString());
//        // Ví dụ: gửi email thông báo cho admin
//    }
//}
