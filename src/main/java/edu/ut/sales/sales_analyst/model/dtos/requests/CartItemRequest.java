package edu.ut.sales.sales_analyst.model.dtos.requests;

import edu.ut.sales.sales_analyst.model.entities.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequest {
    private int quantity;
}
