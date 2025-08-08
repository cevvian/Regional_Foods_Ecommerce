package edu.ut.sales.sales_analyst.model.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.ut.sales.sales_analyst.model.enums.PaymentMethod;
import edu.ut.sales.sales_analyst.model.enums.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

public abstract class PaymentResponse {
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class VNPayResponse {
        public String status;
        public String message;
        public String paymentUrl;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentInfoResponse{
        private String paymentId;
        private PaymentMethod method;
        private int amount;
        private PaymentStatus status;
        private String transactionId;
        private LocalDateTime paidAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private OrderResponse order;
    }
}
