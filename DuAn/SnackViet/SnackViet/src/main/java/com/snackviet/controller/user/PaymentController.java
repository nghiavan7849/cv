package com.snackviet.controller.user;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;


import com.snackviet.configuration.VNPayConfig;
import com.snackviet.model.PaymentResDTO;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    HttpServletRequest request;

    @GetMapping("/createPayment")
    public ResponseEntity<?> createPayment(@RequestParam String paymentMethod) {
        if ("cod".equalsIgnoreCase(paymentMethod)) {
            // Handle Cash on Delivery (COD) method
            PaymentResDTO paymentResDTO = new PaymentResDTO();
            paymentResDTO.setStatus("Ok");
            paymentResDTO.setMessage("Successfully created COD payment.");
            return ResponseEntity.status(HttpStatus.OK).body(paymentResDTO);
        } else if ("online".equalsIgnoreCase(paymentMethod)) {
            // Handle online payment method via VNPay
            long amount = 10000 * 100; // Example amount (should be fetched from your order or cart)

            String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
            String vnp_IpAddr = VNPayConfig.getIpAddress(request);

            String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
            vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_BankCode", "NCB"); // Default bank code for testing
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Payment for order: " + vnp_TxnRef);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            // Generate secure hash
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    try {
                        hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString())).append('&');
                        query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString())).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString())).append('&');
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
            queryUrl += "vnp_SecureHashType=SHA256&vnp_SecureHash=" + vnp_SecureHash;

            PaymentResDTO paymentResDTO = new PaymentResDTO();
            paymentResDTO.setStatus("Ok");
            paymentResDTO.setMessage("Successfully created VNPay payment.");
            paymentResDTO.setURL(VNPayConfig.vnp_PayUrl + "?" + queryUrl);

            return ResponseEntity.status(HttpStatus.OK).body(paymentResDTO);
        }

        // Handle invalid payment method case
        PaymentResDTO paymentResDTO = new PaymentResDTO();
        paymentResDTO.setStatus("Error");
        paymentResDTO.setMessage("Invalid payment method.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(paymentResDTO);
    }
}
