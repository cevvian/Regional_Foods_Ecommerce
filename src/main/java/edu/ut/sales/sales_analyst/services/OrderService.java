package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.OrderMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.OrderCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.OrderItemRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.OrderResponse;
import edu.ut.sales.sales_analyst.model.entities.Customer;
import edu.ut.sales.sales_analyst.model.entities.Order;
import edu.ut.sales.sales_analyst.model.entities.OrderItem;
import edu.ut.sales.sales_analyst.model.entities.Product;
import edu.ut.sales.sales_analyst.model.enums.OrderStatus;
import edu.ut.sales.sales_analyst.repositories.CustomerRepo;
import edu.ut.sales.sales_analyst.repositories.OrderItemRepo;
import edu.ut.sales.sales_analyst.repositories.OrderRepo;
import edu.ut.sales.sales_analyst.repositories.ProductRepo;
import edu.ut.sales.sales_analyst.services.impl.IOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService implements IOrderService {

    private final OrderRepo orderRepo;

    private final OrderMapper orderMapper;

    private final CustomerRepo customerRepo;

    private final ProductRepo productRepo;

    private final OrderItemRepo orderItemRepo;

    public OrderService(OrderRepo orderRepo, OrderMapper orderMapper, CustomerRepo customerRepo, ProductRepo productRepo, OrderItemRepo orderItemRepo) {
        this.orderRepo = orderRepo;
        this.orderMapper = orderMapper;
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
        this.orderItemRepo = orderItemRepo;
    }

    @Override
    public OrderResponse createOrder(OrderCreateRequest orderCreateRequest) {
        // 1. Kiểm tra customer tồn tại
        Customer customer = customerRepo.findByCustomerId(orderCreateRequest.getCustomerId());
        if (customer == null) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }

        // 2. Lấy danh sách sản phẩm từ request
        List<OrderItemRequest> orderItems = orderCreateRequest.getOrderItems();
        if (orderItems == null || orderItems.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_ITEM_LIST_EMPTY);
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        // 3. Tính tổng tiền
        for (OrderItemRequest itemRequest : orderItems) {
            Product product = productRepo.findByProductId(itemRequest.getProductId());
            if (product == null) {
                throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
            }

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        // 4. Tạo Order entity và lưu DB
        Order order = new Order();
        order.setCustomer(customer);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);

        orderRepo.save(order);

        // 5. Tạo và lưu từng OrderItem
        for (OrderItemRequest itemRequest : orderItems) {
            Product product = productRepo.findByProductId(itemRequest.getProductId());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItemRepo.save(orderItem);
        }

        // 6. Trả về DTO
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse getOrder(String orderId) {
        Order order = orderRepo.findByOrderId(orderId);
        if (order == null) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public Page<OrderResponse> getOrdersActive(Pageable pageable) {
        Page<Order> orders = orderRepo.findAll(pageable);
        if (orders.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_LIST_EMPTY);
        }
        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    public Page<OrderResponse> getOrdersByStatusAndActive(Pageable pageable, OrderStatus orderStatus) {
        Page<Order> orders = orderRepo.findByOrderStatusAndActive(orderStatus, true, pageable);
        if (orders.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_LIST_EMPTY);
        }
        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    public Page<OrderResponse> getOrdersByCustomerId(String id, Pageable pageable) {
        Customer customer = customerRepo.findByCustomerId(id);
        if (customer == null) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        Page<Order> orders = orderRepo.findByCustomer_CustomerId(id, pageable);
        if (orders.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_LIST_EMPTY);
        }
        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    public OrderResponse updateOrder(String orderId, OrderCreateRequest orderCreateRequest) {
        Order order = orderRepo.findByOrderId(orderId);
        if (order == null) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }

        // Kiểm tra customer mới
        Customer customer = customerRepo.findByCustomerId(orderCreateRequest.getCustomerId());
        if (customer == null) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        order.setCustomer(customer);

        // Xóa các order item cũ trước
        orderItemRepo.deleteByOrder(order);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : orderCreateRequest.getOrderItems()) {
            Product product = productRepo.findByProductId(itemRequest.getProductId());
            if (product == null) {
                throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItemRepo.save(orderItem);

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        order.setTotalAmount(totalAmount);
        orderRepo.save(order);

        return orderMapper.toOrderResponse(order);
    }


    @Override
    public OrderResponse updateOrderStatus(String orderId, OrderStatus orderStatus) {
        Order order = orderRepo.findByOrderId(orderId);
        if (order == null) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        order.setStatus(orderStatus);
        orderRepo.save(order);
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public Boolean deleteOrder(String orderId) {
        Order order = orderRepo.findByOrderId(orderId);
        if (order == null) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setActive(false);
        orderRepo.save(order);
        return true;
    }

    @Override
    public OrderResponse cancelOrder(String orderId) {
        Order order = orderRepo.findByOrderId(orderId);
        if (order == null) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepo.save(order);
        return orderMapper.toOrderResponse(order);
    }
}
