package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.configs.VNPayConfig;
import edu.ut.sales.sales_analyst.model.dtos.responses.PaymentResponse;
import edu.ut.sales.sales_analyst.model.enums.OrderStatus;
import edu.ut.sales.sales_analyst.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VNPayConfig vnPayConfig;
    private final OrderService orderService;


    public PaymentResponse.VNPayResponse createVnPayPayment(HttpServletRequest request) {
        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
        String bankCode = request.getParameter("bankCode");
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return PaymentResponse.VNPayResponse.builder()
                .status("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }

    public PaymentResponse.VNPayResponse handleCallBack(HttpServletRequest request) {
        String responseCode = request.getParameter("vnp_ResponseCode");

        if ("00".equals(responseCode)) {
            return PaymentResponse.VNPayResponse.builder()
                    .status("00")
                    .message("Giao dịch thành công")
                    .build();
        } else {
            return PaymentResponse.VNPayResponse.builder()
                    .status(responseCode)
                    .message("Giao dịch thất bại hoặc bị hủy")
                    .build();
        }
    }
}
