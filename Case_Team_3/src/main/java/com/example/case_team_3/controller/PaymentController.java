package com.example.case_team_3.controller;

import com.example.case_team_3.model.Room;
import com.example.case_team_3.service.IPayService;
import com.example.case_team_3.service.RoomService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PaymentController {

    @Autowired
    private IPayService payService;

    @Autowired
    private RoomService roomService;

    /**
     * Tạo đơn hàng PayPal cho phòng được chọn và chuyển hướng đến trang phê duyệt thanh toán của PayPal.
     */
    @GetMapping("/rooms/paypal/create-order/{roomId}")
    public String createOrder(@PathVariable("roomId") Integer roomId, HttpServletRequest request) {
        // Lấy thông tin phòng từ database
        Room room = roomService.getRoomById(roomId);
        double amount = room.getRoomPrice();  // Lấy giá phòng
        String currency = "USD"; // Giả sử sử dụng USD (bạn có thể chuyển đổi nếu cần)
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String returnUrl = baseUrl + "/paypal-payment-return?success=true";
        String cancelUrl = baseUrl + "/paypal-payment-return?success=false";

        try {
            Payment payment = payService.createPaymentWithPayPal(
                    amount,
                    currency,
                    "paypal",
                    "sale",
                    "Thanh toán cho phòng có ID: " + room.getRoomId(),
                    cancelUrl,
                    returnUrl);
            // Lấy URL phê duyệt thanh toán từ đối tượng Payment
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

    /**
     * Endpoint callback sau khi PayPal chuyển hướng về.
     * Nếu success=true, thực hiện capture payment; nếu success=false, chuyển sang trang hủy.
     */
    @GetMapping("/paypal-payment-return")
    public String paymentReturn(@RequestParam("success") boolean success,
                                @RequestParam(value = "paymentId", required = false) String paymentId,
                                @RequestParam(value = "PayerID", required = false) String payerId,
                                Model model) {
        if (success) {
            try {
                Payment payment = payService.executePayment(paymentId, payerId);
                model.addAttribute("payment", payment);
                return "payment-success";
            } catch (PayPalRESTException e) {
                e.printStackTrace();
            }
            return "redirect:/";
        } else {
            return "payment-cancel";
        }
    }
}
