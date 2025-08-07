package edu.ut.sales.sales_analyst.model.dtos.requests;

import edu.ut.sales.sales_analyst.model.enums.PaymentMethod;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private PaymentMethod method;
    @Min(value = 10000, message = "Amount must be greater than 10.000")
    private int amount;
    private String orderId;
    private String transactionId;
}
