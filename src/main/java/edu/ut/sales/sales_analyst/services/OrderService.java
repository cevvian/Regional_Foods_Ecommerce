package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.CartItemMapper;
import edu.ut.sales.sales_analyst.mappers.OrderMapper;
import edu.ut.sales.sales_analyst.model.dtos.events.OrderCancelledEvent;
import edu.ut.sales.sales_analyst.model.dtos.events.OrderChangedStatusEvent;
import edu.ut.sales.sales_analyst.model.dtos.events.OrderCreatedEvent;
import edu.ut.sales.sales_analyst.model.dtos.requests.OrderCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.OrderItemRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.*;
import edu.ut.sales.sales_analyst.model.dtos.responses.OrderResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.PaymentResponse;
import edu.ut.sales.sales_analyst.model.entities.*;
import edu.ut.sales.sales_analyst.model.enums.OrderStatus;
import edu.ut.sales.sales_analyst.producer.EventProducer;
import edu.ut.sales.sales_analyst.model.enums.PaymentMethod;
import edu.ut.sales.sales_analyst.repositories.*;
import edu.ut.sales.sales_analyst.services.impl.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final EventProducer eventProducer;
    private final PaymentService paymentService;
    private final CartItemMapper cartItemMapper;
    private final CartService cartService;

    public OrderService(OrderRepo orderRepo, OrderMapper orderMapper, UserRepo userRepo, CartService cartService,
                        ProductRepo productRepo, OrderItemRepo orderItemRepo, AddressRepo addressRepo,
                        EventProducer eventProducer, PaymentService paymentService, CartItemMapper cartItemMapper) {
        this.orderRepo = orderRepo;
        this.orderMapper = orderMapper;
        this.userRepo = userRepo;
        this.cartService = cartService;
        this.productRepo = productRepo;
        this.orderItemRepo = orderItemRepo;
        this.addressRepo = addressRepo;
        this.eventProducer = eventProducer;
        this.paymentService = paymentService;
        this.cartItemMapper = cartItemMapper;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderCartCreationRequest request) {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        User customer = validateCustomer(request.getCustomerId());
        Address address = validateAddress(request.getAddressId(), customer.getUserId());

        // Nếu có cartItems truyền vào thì convert sang orderItems
        List<OrderItemRequest> orderItemRequests;
        if (request.getCartItems() != null && !request.getCartItems() .isEmpty()) {
            List<CartItem> cartItems = cartItemMapper.toCartItem(request.getCartItems());
            orderItemRequests = getOrderItemsFromCart(cartItems);
            // Ghi đè lại orderItems trong request
            orderCreateRequest.setOrderItems(orderItemRequests);
        }

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
        order.setOrderItems(orderItemRepo.findByOrder(order));

        log.info("Method: {}", request.getMethod());
        if (request.getMethod().equals(PaymentMethod.CASH)) {
            PaymentRequest paymentRequest = PaymentRequest.builder()
                    .orderId(order.getOrderId())
                    .method(request.getMethod())
                    .amount(toInt(order.getTotalAmount(), RoundingMode.HALF_UP))
                    .build();
            PaymentResponse.PaymentInfoResponse payment = paymentService.createPayment(paymentRequest);
            OrderCreatedEvent event = new OrderCreatedEvent(order.getOrderId(), payment.getPaymentId());
            eventProducer.sendOrderCreatedEvent(event);
        } else {
            throw new AppException(ErrorCode.PAYMENT_METHOD_UNSUPPORTED);
        }

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
        Page<Order> orders = orderRepo.findByActive(pageable, true);
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
    public OrderResponse updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orderRepo.findByOrderId(orderId);
        if (order == null) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }

        OrderStatus currentStatus = order.getStatus();
        if (!currentStatus.canTransitionTo(newStatus)) {
            throw new AppException(ErrorCode.ORDER_INVALID_STATUS_TRANSITION);
        }

        order.setStatus(newStatus);
        orderRepo.save(order);
        if (newStatus == OrderStatus.CANCELLED) {
            PaymentResponse.PaymentInfoResponse paymentResponse = paymentService.getPaymentsByOrderIdAndPaid(orderId);
            OrderCancelledEvent orderCancelledEvent = new OrderCancelledEvent(orderId, paymentResponse.getPaymentId());
            eventProducer.sendOrderCancelledEvent(orderCancelledEvent);
            return orderMapper.toOrderResponse(order);
        }
        OrderChangedStatusEvent orderChangedStatusEvent =
                new OrderChangedStatusEvent(orderId, newStatus);
        eventProducer.sendOrderChangedStatusEvent(orderChangedStatusEvent);
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
        PaymentResponse.PaymentInfoResponse paymentResponse = paymentService.getPaymentsByOrderIdAndPaid(orderId);
        OrderCancelledEvent orderCancelledEvent = new OrderCancelledEvent(orderId, paymentResponse.getPaymentId());
        eventProducer.sendOrderCancelledEvent(orderCancelledEvent);
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
        PaymentResponse.PaymentInfoResponse paymentResponse = paymentService.getPaymentsByOrderIdAndPaid(orderId);
        OrderCancelledEvent orderCancelledEvent = new OrderCancelledEvent(orderId, paymentResponse.getPaymentId());
        eventProducer.sendOrderCancelledEvent(orderCancelledEvent);
        return orderMapper.toOrderResponse(order);
    }

    public int toInt(BigDecimal value, RoundingMode roundingMode) {
        if (value == null) {
            throw new AppException(ErrorCode.ORDER_AMOUNT_REQUIRED);
        }
        BigDecimal rounded = value.setScale(0, roundingMode); // làm tròn
        return rounded.intValueExact(); // ép kiểu và kiểm tra tràn
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
            orderItem.setUnitPrice(productMap.get(productId).getPrice());
            orderItemRepo.save(orderItem);
        }
    }

    private List<OrderItemRequest> getOrderItemsFromCart(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> OrderItemRequest.builder()
                        .productId(cartItem.getProduct().getProductId())
                        .quantity(cartItem.getQuantity())
                        .build())
                .toList();
    }
}
