package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.requests.OrderAndPaymentRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.OrderCartCreationRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.OrderCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.PaymentRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.OrderResponse;
import edu.ut.sales.sales_analyst.model.entities.CartItem;
import edu.ut.sales.sales_analyst.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderAndPaymentRequest request);
//    OrderResponse createOrderFromCart(OrderCartCreationRequest creationRequest, PaymentRequest paymentRequest);
    OrderResponse getOrder(String orderId);
    Page<OrderResponse> getOrdersActive(Pageable pageable);
    Page<OrderResponse> getOrdersByStatusAndActive(Pageable pageable, OrderStatus orderStatus);
    Page<OrderResponse> getOrdersByCustomerId(String id, Pageable pageable);
    OrderResponse updateOrder(String orderId, OrderCreateRequest orderCreateRequest);
    OrderResponse updateOrderStatus(String orderId, OrderStatus orderStatus);
    Boolean deleteOrder(String orderId);
    OrderResponse cancelOrder(String orderId);
}
