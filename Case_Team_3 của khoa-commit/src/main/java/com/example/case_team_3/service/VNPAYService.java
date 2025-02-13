//package com.example.case_team_3.service;
//
//import com.example.case_team_3.config.VNPAYConfig;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@Service
//public class VNPAYService {
//
//    /**
//     * Tạo URL thanh toán VNPay dựa trên các tham số đơn hàng.
//     * Lưu ý: Số tiền cần được nhân với 100 để loại bỏ phần thập phân.
//     */
//    public String createOrder(HttpServletRequest request, int amount, String orderInfo, String urlReturn) {
//        String vnp_Version = "2.1.0";
//        String vnp_Command = "pay";
//        String vnp_TxnRef = VNPAYConfig.getRandomNumber(8);
//        String vnp_IpAddr = VNPAYConfig.getIpAddress(request);
//        String vnp_TmnCode = VNPAYConfig.vnp_TmnCode;
//        String orderType = "other"; // Loại đơn hàng
//
//        Map<String, String> vnp_Params = new HashMap<>();
//        vnp_Params.put("vnp_Version", vnp_Version);
//        vnp_Params.put("vnp_Command", vnp_Command);
//        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
//        // VNPay yêu cầu số tiền nhân với 100 (ví dụ: 299999 VND -> 29999900)
//        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
//        vnp_Params.put("vnp_CurrCode", "VND");
//        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
//        vnp_Params.put("vnp_OrderInfo", orderInfo);
//        vnp_Params.put("vnp_OrderType", orderType);
//        vnp_Params.put("vnp_Locale", "vn");
//
//        // Nối base URL với vnp_Returnurl đã định nghĩa
//        urlReturn += VNPAYConfig.vnp_Returnurl;
//        vnp_Params.put("vnp_ReturnUrl", urlReturn);
//        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//
//        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String vnp_CreateDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//
//        cld.add(Calendar.MINUTE, 15);
//        String vnp_ExpireDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
//
//        // Sắp xếp tham số theo thứ tự key tăng dần
//        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder query = new StringBuilder();
//        Iterator<String> itr = fieldNames.iterator();
//        while (itr.hasNext()) {
//            String fieldName = itr.next();
//            String fieldValue = vnp_Params.get(fieldName);
//            if (fieldValue != null && fieldValue.length() > 0) {
//                try {
//                    hashData.append(fieldName)
//                            .append('=')
//                            .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
//                            .append('=')
//                            .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                if (itr.hasNext()) {
//                    hashData.append('&');
//                    query.append('&');
//                }
//            }
//        }
//        String queryUrl = query.toString();
//        String salt = VNPAYConfig.vnp_HashSecret;
//        String vnp_SecureHash = VNPAYConfig.hmacSHA512(salt, hashData.toString());
//        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//        String paymentUrl = VNPAYConfig.vnp_PayUrl + "?" + queryUrl;
//        return paymentUrl;
//    }
//
//    /**
//     * Xử lý phản hồi trả về từ VNPay.
//     * Trả về:
//     *  1: thanh toán thành công,
//     *  0: thanh toán thất bại,
//     * -1: hash không hợp lệ.
//     */
//    public int orderReturn(HttpServletRequest request) {
//        Map<String, String> fields = new HashMap<>();
//        Enumeration<String> parameterNames = request.getParameterNames();
//        while (parameterNames.hasMoreElements()){
//            String fieldName = parameterNames.nextElement();
//            String fieldValue = request.getParameter(fieldName);
//            if (fieldValue != null && fieldValue.length() > 0) {
//                fields.put(fieldName, fieldValue);
//            }
//        }
//        // Lấy giá trị secure hash từ request
//        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
//        // Loại bỏ các trường liên quan đến secure hash khỏi map để tính hash lại
//        fields.remove("vnp_SecureHashType");
//        fields.remove("vnp_SecureHash");
//        String signValue = VNPAYConfig.hashAllFields(fields);
//        if (signValue.equals(vnp_SecureHash)) {
//            // Theo tài liệu VNPay, vnp_ResponseCode "00" nghĩa là thanh toán thành công
//            if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
//                return 1;
//            } else {
//                return 0;
//            }
//        } else {
//            return -1;
//        }
//    }
//}
