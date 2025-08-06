package edu.ut.sales.sales_analyst.model.dtos.responses;

import lombok.*;

public abstract class PaymentResponse {
    @Builder
    public static class VNPayResponse {
        public String status;
        public String message;
        public String paymentUrl;
    }
}
