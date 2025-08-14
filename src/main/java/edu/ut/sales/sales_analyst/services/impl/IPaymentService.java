package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.requests.PaymentRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.PaymentResponse;
import edu.ut.sales.sales_analyst.model.enums.PaymentStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPaymentService {
    Page<PaymentResponse.PaymentInfoResponse> getAllPayments(Pageable pageable);
    Page<PaymentResponse.PaymentInfoResponse> getPaymentsByOrderId(String orderId, Pageable pageable);
    PaymentResponse.PaymentInfoResponse getPaymentById(String paymentId);
    PaymentResponse.PaymentInfoResponse updatePaymentStatus(PaymentStatus status, String paymentId);
    PaymentResponse.PaymentInfoResponse createPayment(PaymentRequest request);
    PaymentResponse.VNPayResponse handleCallBack(HttpServletRequest request);
    PaymentResponse.VNPayResponse createVnPayPayment(HttpServletRequest request);
    PaymentResponse.PaymentInfoResponse getPaymentsByOrderIdAndPaid(String orderId);
}
