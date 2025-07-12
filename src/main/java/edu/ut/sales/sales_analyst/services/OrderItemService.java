package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.model.dtos.requests.OrderItemRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.OrderItemResponse;
import edu.ut.sales.sales_analyst.services.impl.IOrderItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService implements IOrderItemService {
    @Override
    public OrderItemResponse createOrderItem(OrderItemRequest orderItemRequest) {
        return null;
    }

    @Override
    public OrderItemResponse getOrderItem(String orderItemId) {
        return null;
    }

    @Override
    public Page<OrderItemResponse> getOrderItems(Pageable pageable) {
        return null;
    }

    @Override
    public List<OrderItemResponse> getOrderItemsByOrderId(String orderId) {
        return List.of();
    }

    @Override
    public OrderItemResponse updateOrderItem(OrderItemRequest orderItemRequest) {
        return null;
    }

    @Override
    public Boolean deleteOrderItem(String orderItemId) {
        return null;
    }
}
