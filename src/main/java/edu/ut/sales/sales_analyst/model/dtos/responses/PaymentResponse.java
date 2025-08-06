package edu.ut.sales.sales_analyst.model.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

public abstract class PaymentResponse {
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class VNPayResponse {
        public String status;
        public String message;
        public String paymentUrl;
    }
}
