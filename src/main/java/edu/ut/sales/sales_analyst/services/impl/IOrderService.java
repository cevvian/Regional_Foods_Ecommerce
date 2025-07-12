package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.requests.OrderCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.OrderResponse;
import edu.ut.sales.sales_analyst.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderService {
    OrderResponse createOrder(OrderCreateRequest orderCreateRequest);
    OrderResponse getOrder(String orderId);
    Page<OrderResponse> getOrdersActive(Pageable pageable);
    Page<OrderResponse> getOrdersByStatusAndActive(Pageable pageable, OrderStatus orderStatus);
    Page<OrderResponse> getOrdersByCustomerId(String id, Pageable pageable);
    OrderResponse updateOrder(String orderId, OrderCreateRequest orderCreateRequest);
    OrderResponse updateOrderStatus(String orderId, OrderStatus orderStatus);
    Boolean deleteOrder(String orderId);
    OrderResponse cancelOrder(String orderId);
}
