package edu.ut.sales.sales_analyst.model.dtos.requests;

import edu.ut.sales.sales_analyst.model.dtos.responses.OrderItemResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequest {
    @NotBlank(message = "Customer id can not be blank")
    String customerId;

    @NotBlank(message = "Address id can not be blank")
    String addressId;

    @NotEmpty(message = "Order must contain at least one item")
    List<OrderItemRequest> orderItems;
}
