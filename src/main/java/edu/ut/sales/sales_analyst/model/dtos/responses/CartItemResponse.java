package edu.ut.sales.sales_analyst.model.dtos.responses;

import edu.ut.sales.sales_analyst.model.entities.Cart;
import edu.ut.sales.sales_analyst.model.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private String cartItemId;
    private int quantity;
    private Cart cart;
    private Product product;
}