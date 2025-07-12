package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.requests.OrderItemRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.OrderItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderItemService {
    OrderItemResponse createOrderItem(OrderItemRequest orderItemRequest);
    OrderItemResponse getOrderItem(String orderItemId);
    Page<OrderItemResponse> getOrderItems(Pageable pageable);
    List<OrderItemResponse> getOrderItemsByOrderId(String orderId);
    OrderItemResponse updateOrderItem(OrderItemRequest orderItemRequest);
    Boolean deleteOrderItem(String orderItemId);
}
