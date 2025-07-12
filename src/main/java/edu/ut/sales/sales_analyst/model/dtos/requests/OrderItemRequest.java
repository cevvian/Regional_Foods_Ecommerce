package edu.ut.sales.sales_analyst.model.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {

    @NotBlank(message = "Product id can not be blank")
    String productId;

    @Min(value = 1, message = "Order item must contain at least one product")
    int quantity;
}
