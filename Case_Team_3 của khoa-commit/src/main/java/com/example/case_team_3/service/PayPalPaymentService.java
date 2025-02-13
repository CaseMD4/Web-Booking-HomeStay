package com.example.case_team_3.service;

import com.example.case_team_3.paypal.PayPalClient;


import jakarta.persistence.criteria.Order;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalPaymentService {

    @Autowired
    private PayPalClient payPalClient;

    /**
     * Tạo đơn hàng PayPal và trả về URL phê duyệt thanh toán.
     */
    public String createOrder(double amount, String currency, String returnUrl, String cancelUrl) throws IOException {
        OrdersCreateRequest request = new OrdersCreateRequest();
        request.prefer("return=representation");
        request.requestBody(buildRequestBody(amount, currency, returnUrl, cancelUrl));

        HttpResponse<Order> response = payPalClient.client().execute(request);
        for (LinkDescription link : response.result().links()) {
            if ("approve".equalsIgnoreCase(link.rel())) {
                return link.href();
            }
        }
        throw new RuntimeException("No approval link found from PayPal");
    }

    private OrderRequest buildRequestBody(double amount, String currency, String returnUrl, String cancelUrl) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl);
        orderRequest.applicationContext(applicationContext);

        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                .amountWithBreakdown(new AmountWithBreakdown()
                        .currencyCode(currency)
                        .value(formatAmount(amount)));
        purchaseUnits.add(purchaseUnitRequest);
        orderRequest.purchaseUnits(purchaseUnits);

        return orderRequest;
    }

    // Định dạng số tiền thành chuỗi với 2 chữ số thập phân
    private String formatAmount(double amount) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(amount);
    }

    /**
     * Capture đơn hàng sau khi người dùng phê duyệt thanh toán.
     * Trả về true nếu giao dịch thành công.
     */
    public boolean captureOrder(String orderId) throws IOException {
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
        request.requestBody(new OrderRequest());
        HttpResponse<Order> response = payPalClient.client().execute(request);
        return "COMPLETED".equalsIgnoreCase(response.result().status());
    }
}
