package edu.ut.sales.sales_analyst.model.dtos.responses;

import edu.ut.sales.sales_analyst.model.enums.OrderStatus;
import edu.ut.sales.sales_analyst.model.enums.PaymentMethod;
import edu.ut.sales.sales_analyst.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private String orderId;
    private UserResponse userResponse;
    private AddressResponse addressResponse;
    private List<OrderItemResponse> orderItemResponses;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private Date orderDate;
    private PaymentMethod paymentMethod;
}
