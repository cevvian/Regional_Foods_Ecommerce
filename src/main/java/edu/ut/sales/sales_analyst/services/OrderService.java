package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.OrderMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.OrderCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.OrderItemRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.OrderResponse;
import edu.ut.sales.sales_analyst.model.entities.*;
import edu.ut.sales.sales_analyst.model.enums.OrderStatus;
import edu.ut.sales.sales_analyst.repositories.*;
import edu.ut.sales.sales_analyst.services.impl.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrderService implements IOrderService {

    private final OrderRepo orderRepo;

    private final OrderMapper orderMapper;

    private final UserRepo userRepo;

    private final ProductRepo productRepo;

    private final OrderItemRepo orderItemRepo;
    private final AddressRepo addressRepo;

    public OrderService(OrderRepo orderRepo, OrderMapper orderMapper, UserRepo userRepo,
                        ProductRepo productRepo, OrderItemRepo orderItemRepo, AddressRepo addressRepo) {
        this.orderRepo = orderRepo;
        this.orderMapper = orderMapper;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.orderItemRepo = orderItemRepo;
        this.addressRepo = addressRepo;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest orderCreateRequest) {
        User customer = validateCustomer(orderCreateRequest.getCustomerId());
        Address address = validateAddress(orderCreateRequest.getAddressId(), customer.getUserId());

        Map<String, Integer> quantityMap = groupAndValidateOrderItems(orderCreateRequest.getOrderItems());

        Map<String, Product> productMap = new HashMap<>();
        BigDecimal totalAmount = processOrderItemsAndCalculateTotal(quantityMap, productMap);

        Order order = new Order();
        order.setUser(customer);
        order.setAddress(address);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);
        orderRepo.save(order);

        createOrderItems(order, quantityMap, productMap);

        log.info("Created order {} for user {}", order.getOrderId(), customer.getUserId());
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
        Page<Order> orders = orderRepo.findByActiveTrue(pageable);
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
        User customer = userRepo.findByUserId(id);
        if (customer == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        Page<Order> orders = orderRepo.findByUser_UserId(id, pageable);
        if (orders.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_LIST_EMPTY);
        }
        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(String orderId, OrderCreateRequest orderCreateRequest) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // Chỉ cho sửa nếu đơn hàng đang chờ xử lý
        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new AppException(ErrorCode.ORDER_NOT_ALLOWED_UPDATE);
        }

        // Kiểm tra customer mới
        User newCustomer = validateCustomer(orderCreateRequest.getCustomerId());
        order.setUser(newCustomer);

        // Validate address mới
        Address newAddress = validateAddress(orderCreateRequest.getAddressId(), newCustomer.getUserId());
        order.setAddress(newAddress);

        // Trả lại tồn kho cũ khi xóa các order item cũ trước
        List<OrderItem> oldItems = orderItemRepo.findByOrder(order);
        for (OrderItem item : oldItems) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepo.save(product);
        }
        orderItemRepo.deleteAll(oldItems);

        // Validate và tính lại order mới
        Map<String, Integer> quantityMap = groupAndValidateOrderItems(orderCreateRequest.getOrderItems());
        Map<String, Product> productMap = new HashMap<>();
        BigDecimal totalAmount = processOrderItemsAndCalculateTotal(quantityMap, productMap);

//        Tạo order item mới
        createOrderItems(order, quantityMap, productMap);

//        Cập nhật tổng tiền và lưu
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

    private User validateCustomer(String customerId) {
        User customer = userRepo.findByUserId(customerId);
        if (customer == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        return customer;
    }

    private Address validateAddress(String addressId, String userId) {
        return addressRepo.findByAddressIdAndUser_UserId(addressId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
    }

    private Map<String, Integer> groupAndValidateOrderItems(List<OrderItemRequest> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_ITEM_LIST_EMPTY);
        }

        Map<String, Integer> quantityMap = new HashMap<>();
        for (OrderItemRequest item : orderItems) {
            if (item.getQuantity() <= 0) {
                throw new AppException(ErrorCode.PRODUCT_INVALID_QUANTITY);
            }
            quantityMap.merge(item.getProductId(), item.getQuantity(), Integer::sum);
        }
        return quantityMap;
    }

    private BigDecimal processOrderItemsAndCalculateTotal(Map<String, Integer> quantityMap, Map<String, Product> productMap) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Map.Entry<String, Integer> entry : quantityMap.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();

            Product product = productRepo.findByIdForUpdate(productId)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            if (product.isDeleted()) {
                throw new AppException(ErrorCode.PRODUCT_DELETED);
            }

            if (product.getStockQuantity() < quantity) {
                throw new AppException(ErrorCode.PRODUCT_OUT_OF_STOCK);
            }

            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepo.save(product);

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            totalAmount = totalAmount.add(itemTotal);

            productMap.put(productId, product);
        }

        return totalAmount;
    }

    private void createOrderItems(Order order, Map<String, Integer> quantityMap, Map<String, Product> productMap) {
        for (Map.Entry<String, Integer> entry : quantityMap.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(productMap.get(productId));
            orderItem.setQuantity(quantity);
            orderItemRepo.save(orderItem);
        }
    }

}
