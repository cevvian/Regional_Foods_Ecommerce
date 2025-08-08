package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.configs.VNPayConfig;
import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.PaymentMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.PaymentRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.PaymentResponse;
import edu.ut.sales.sales_analyst.model.entities.Order;
import edu.ut.sales.sales_analyst.model.entities.Payment;
import edu.ut.sales.sales_analyst.model.enums.OrderStatus;
import edu.ut.sales.sales_analyst.model.enums.PaymentMethod;
import edu.ut.sales.sales_analyst.model.enums.PaymentStatus;
import edu.ut.sales.sales_analyst.repositories.OrderRepo;
import edu.ut.sales.sales_analyst.repositories.PaymentRepo;
import edu.ut.sales.sales_analyst.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VNPayConfig vnPayConfig;
    private final OrderService orderService;
    private final PaymentMapper paymentMapper;
    private final OrderRepo orderRepo;
    private final PaymentRepo paymentRepo;

    public PaymentResponse.VNPayResponse createVnPayPayment(HttpServletRequest request) {
        String orderId = request.getParameter("orderId");
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        long amount = order.getTotalAmount().longValue() * 100L;
        String bankCode = request.getParameter("bankCode");
        PaymentMethod paymentMethod = getPaymentMethod(request);
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        String transactionId = vnpParamsMap.get("vnp_TxnRef");
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(orderId)
                .amount((int) amount)
                .method(paymentMethod)
                .transactionId(transactionId)
                .build();
        log.warn("Payment method: {}", paymentMethod);

        createPayment(paymentRequest);

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
        String transactionId = request.getParameter("vnp_TxnRef");
        Payment payment = paymentRepo.findPaymentByTransactionId(transactionId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        if ("00".equals(responseCode)) {
            updatePaymentStatus(PaymentStatus.PAID, payment.getPaymentId());
            orderService.updateOrderStatus(payment.getOrder().getOrderId(), OrderStatus.COMPLETED);
            return PaymentResponse.VNPayResponse.builder()
                    .status("00")
                    .message("Giao dịch thành công")
                    .build();
        } else {
            updatePaymentStatus(PaymentStatus.FAILED, payment.getPaymentId());
            return PaymentResponse.VNPayResponse.builder()
                    .status(responseCode)
                    .message("Giao dịch thất bại hoặc bị hủy")
                    .build();
        }
    }

    public PaymentResponse.PaymentInfoResponse createPayment(PaymentRequest request){
        Order order = orderRepo.findById(request.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if(order.getStatus() == OrderStatus.COMPLETED)
            throw new AppException(ErrorCode.ORDER_ALREADY_COMPLETED);
        Payment payment = Payment.builder()
                .amount(request.getAmount())
                .order(order)
                .method(request.getMethod())
                .status(PaymentStatus.PROCESSING)
                .transactionId(request.getTransactionId())
                .build();
        paymentRepo.save(payment);

        return paymentMapper.toPaymentInfoResponse(payment);
    }

    public PaymentResponse.PaymentInfoResponse updatePaymentStatus(PaymentStatus status, String paymentId){
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        payment.setStatus(status);
        paymentRepo.save(payment);
        return paymentMapper.toPaymentInfoResponse(payment);
    }

    public PaymentResponse.PaymentInfoResponse getPaymentById(String paymentId) {
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        return paymentMapper.toPaymentInfoResponse(payment);
    }

    public Page<PaymentResponse.PaymentInfoResponse> getPaymentsByOrderId(String orderId, Pageable pageable) {
        Page<Payment> payments = paymentRepo.findByOrder_OrderId(orderId, pageable);
        return payments.map(paymentMapper::toPaymentInfoResponse);
    }

    public Page<PaymentResponse.PaymentInfoResponse> getAllPayments(Pageable pageable) {
        Page<Payment> payments = paymentRepo.findAll(pageable);
        return payments.map(paymentMapper::toPaymentInfoResponse);
    }

    private PaymentMethod getPaymentMethod(HttpServletRequest request) {
        try{
            String method = request.getParameter("method");
            return PaymentMethod.valueOf(method.toUpperCase());
        }
        catch (Exception e) {
            throw new AppException(ErrorCode.PAYMENT_METHOD_UNSUPPORTED);
        }
    }
//
//    public boolean verifyVNPaySignature(HttpServletRequest request) {
//        Map<String, String> params = VNPayUtil.getParameterMap(request);
//        String receivedHash = params.remove("vnp_SecureHash");
//
//        String hashData = VNPayUtil.getPaymentURL(params, false);
//        String expectedHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
//
//        return expectedHash.equals(receivedHash);
//    }
}
