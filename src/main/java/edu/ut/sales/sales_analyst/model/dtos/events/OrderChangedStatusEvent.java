package edu.ut.sales.sales_analyst.model.dtos.events;

import edu.ut.sales.sales_analyst.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderChangedStatusEvent {
    private String orderId;
    private OrderStatus orderStatus;
}
