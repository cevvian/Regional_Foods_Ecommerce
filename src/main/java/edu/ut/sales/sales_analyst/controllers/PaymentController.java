package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.model.dtos.requests.PaymentRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.*;
import edu.ut.sales.sales_analyst.model.enums.PaymentStatus;
import edu.ut.sales.sales_analyst.services.EmailService;
import edu.ut.sales.sales_analyst.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/payment")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "APIs for user payment order")
public class PaymentController {
    private final PaymentService paymentService;
    private final EmailService emailService;

    //http://localhost:8080/api/v1/payment/vn-pay?amount=237008&bankCode=NCB&method=VNPAY&orderId=38545e7b-a50d-49be-944e-947ed9d5e5d0
    //Thông tin thẻ test: https://sandbox.vnpayment.vn/apis/vnpay-demo/
    @Operation(summary = "payment order", description = "Payment an order with VNPay")
    @GetMapping("/vn-pay")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseAPI<PaymentResponse.VNPayResponse> pay(HttpServletRequest request) {
            PaymentResponse.VNPayResponse response = paymentService.createVnPayPayment(request);
            return new ResponseAPI<>("Payment successfully", HttpStatus.OK, response);
    }

    //test
    @PostMapping("/send-email")
    public void sendEmail(@RequestParam(name = "email") String email,
                          @RequestBody @Valid PaymentRequest request) throws MessagingException {
        emailService.sendInvoiceEmail(email, request);
    }

    @Operation(summary = "handle VNPay call back", description = "Handle VNPay call back after make a transaction")
    @GetMapping("/vn-pay-callback")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseAPI<PaymentResponse.VNPayResponse> payCallbackHandler(HttpServletRequest request) {
            PaymentResponse.VNPayResponse response = paymentService.handleCallBack(request);
            return new ResponseAPI<>("Handle successfully", HttpStatus.OK, response);
    }

    @Operation(summary = "create payment", description = "Create a new payment")
    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseAPI<PaymentResponse.PaymentInfoResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
            PaymentResponse.PaymentInfoResponse response = paymentService.createPayment(request);
            return new ResponseAPI<>("Create payment successfully", HttpStatus.CREATED, response);
    }

    @Operation(summary = "update payment status", description = "Update a existing payment status")
    @PutMapping("/status/{paymentId}")
    public ResponseAPI<PaymentResponse.PaymentInfoResponse> updatePayment(@PathVariable String paymentId,
                                                                          @RequestParam PaymentStatus status) {
            PaymentResponse.PaymentInfoResponse response = paymentService.updatePaymentStatus(status, paymentId);
            return new ResponseAPI<>("Update payment successfully", HttpStatus.OK, response);
    }

    @Operation(summary = "get payment by id", description = "Get an existing payment by id")
    @GetMapping("/{paymentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseAPI<PaymentResponse.PaymentInfoResponse> getPaymentById(@PathVariable String paymentId) {
            PaymentResponse.PaymentInfoResponse response = paymentService.getPaymentById(paymentId);
            return new ResponseAPI<>("Get payment successfully", HttpStatus.OK, response);
    }

    @Operation(summary = "Get all payments by orderId")
    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseAPI<List<PaymentResponse.PaymentInfoResponse>> getAllPaymentByOrderId(
            @PathVariable String orderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PaymentResponse.PaymentInfoResponse> responses = paymentService.getPaymentsByOrderId(orderId,pageable);

        PageMeta meta = PageMeta.builder()
                .page(responses.getNumber())
                .size(responses.getSize())
                .totalElements(responses.getTotalElements())
                .totalPages(responses.getTotalPages())
                .last(responses.isLast())
                .build();

        return new ResponseAPI<>("Get payments successfully", HttpStatus.OK, responses.getContent(), meta);
    }

    @Operation(summary = "Get all payments")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseAPI<List<PaymentResponse.PaymentInfoResponse>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PaymentResponse.PaymentInfoResponse> responses = paymentService.getAllPayments(pageable);

        PageMeta meta = PageMeta.builder()
                .page(responses.getNumber())
                .size(responses.getSize())
                .totalElements(responses.getTotalElements())
                .totalPages(responses.getTotalPages())
                .last(responses.isLast())
                .build();

        return new ResponseAPI<>("Get payments successfully", HttpStatus.OK, responses.getContent(), meta);
    }
}
