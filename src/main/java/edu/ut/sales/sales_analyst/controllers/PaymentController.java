package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.model.dtos.responses.OrderResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.model.dtos.responses.PaymentResponse;
import edu.ut.sales.sales_analyst.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    //http://localhost:8080/api/v1/payment/vn-pay?amount=237008&bankCode=NCB
    @GetMapping("/vn-pay")
    public ResponseAPI<PaymentResponse.VNPayResponse> pay(HttpServletRequest request) {
        return new ResponseAPI<>("Success",HttpStatus.OK , paymentService.createVnPayPayment(request));
    }

    @Operation(summary = "handle VNPay call back", description = "Handle VNPay call back after make a transaction")
    @GetMapping("/vn-pay-callback")
    public ResponseAPI<PaymentResponse.VNPayResponse> payCallbackHandler(HttpServletRequest request) {
        try {
            PaymentResponse.VNPayResponse response = paymentService.handleCallBack(request);
            return new ResponseAPI<>("Handle successfully", HttpStatus.OK, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
