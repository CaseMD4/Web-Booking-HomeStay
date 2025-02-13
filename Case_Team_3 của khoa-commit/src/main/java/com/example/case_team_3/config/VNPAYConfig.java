//package com.example.case_team_3.config;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import javax.servlet.http.HttpServletRequest;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//
//public class VNPAYConfig {
//    // Địa chỉ cổng thanh toán VNPay (Sandbox)
//    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
//    // Đường dẫn callback (sẽ được nối với base URL của ứng dụng)
//    public static String vnp_Returnurl = "/vnpay-payment-return";
//    // Thông tin do VNPay cung cấp (nhớ thay thế bằng thông tin thực tế)
//    public static String vnp_TmnCode = "YOUR_TMN_CODE";         // Ví dụ: "ABCDEF01"
//    public static String vnp_HashSecret = "YOUR_HASH_SECRET";     // Ví dụ: "1234567890ABCDEF1234567890ABCDEF"
//    public static String vnp_apiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";
//
//    /**
//     * Tạo hash từ các trường trả về
//     */
//    public static String hashAllFields(Map<String, String> fields) {
//        List<String> fieldNames = new ArrayList<>(fields.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder sb = new StringBuilder();
//        Iterator<String> itr = fieldNames.iterator();
//        while (itr.hasNext()) {
//            String fieldName = itr.next();
//            String fieldValue = fields.get(fieldName);
//            if (fieldValue != null && fieldValue.length() > 0) {
//                sb.append(fieldName).append("=").append(fieldValue);
//                if (itr.hasNext()) {
//                    sb.append("&");
//                }
//            }
//        }
//        return hmacSHA512(vnp_HashSecret, sb.toString());
//    }
//
//    /**
//     * Tạo mã HMAC SHA512
//     */
//    public static String hmacSHA512(final String key, final String data) {
//        try {
//            if (key == null || data == null) {
//                throw new NullPointerException();
//            }
//            final Mac hmac512 = Mac.getInstance("HmacSHA512");
//            byte[] hmacKeyBytes = key.getBytes(StandardCharsets.UTF_8);
//            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
//            hmac512.init(secretKey);
//            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
//            byte[] result = hmac512.doFinal(dataBytes);
//            StringBuilder sb = new StringBuilder(2 * result.length);
//            for (byte b : result) {
//                sb.append(String.format("%02x", b & 0xff));
//            }
//            return sb.toString();
//        } catch (Exception ex) {
//            return "";
//        }
//    }
//
//    /**
//     * Lấy địa chỉ IP của request
//     */
//    public static String getIpAddress(HttpServletRequest request) {
//        String ipAddress;
//        try {
//            ipAddress = request.getHeader("X-FORWARDED-FOR");
//            if (ipAddress == null) {
//                ipAddress = request.getRemoteAddr();
//            }
//        } catch (Exception e) {
//            ipAddress = "Invalid IP:" + e.getMessage();
//        }
//        return ipAddress;
//    }
//
//    /**
//     * Tạo số ngẫu nhiên với độ dài cho trước
//     */
//    public static String getRandomNumber(int len) {
//        Random rnd = new Random();
//        StringBuilder sb = new StringBuilder(len);
//        String chars = "0123456789";
//        for (int i = 0; i < len; i++) {
//            sb.append(chars.charAt(rnd.nextInt(chars.length())));
//        }
//        return sb.toString();
//    }
//}
