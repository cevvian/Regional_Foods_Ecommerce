package edu.ut.sales.sales_analyst.model.dtos.events;

import edu.ut.sales.sales_analyst.model.dtos.requests.CartItemRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {
    private String orderId;
    private List<CartItemRequest> cartItems;
}