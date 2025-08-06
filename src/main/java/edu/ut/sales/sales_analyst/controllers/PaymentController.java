package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.model.dtos.responses.PaymentResponse;
import edu.ut.sales.sales_analyst.services.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/vn-pay-callback")
    public ResponseAPI<PaymentResponse.VNPayResponse> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            return new ResponseAPI<>("Success", HttpStatus.OK,
                    PaymentResponse.VNPayResponse.builder()
                            .status("00")
                            .message("Success")
                            .paymentUrl("")
                            .build()
            );
        } else {
            return new ResponseAPI<>("Failed",HttpStatus.BAD_REQUEST, null);
        }
    }
}
