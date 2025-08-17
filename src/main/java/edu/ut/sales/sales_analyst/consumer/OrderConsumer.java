package edu.ut.sales.sales_analyst.consumer;

import edu.ut.sales.sales_analyst.model.dtos.events.*;
import edu.ut.sales.sales_analyst.model.dtos.requests.*;
import edu.ut.sales.sales_analyst.model.dtos.responses.*;
import edu.ut.sales.sales_analyst.model.enums.OrderStatus;
import edu.ut.sales.sales_analyst.model.enums.PaymentStatus;
import edu.ut.sales.sales_analyst.services.*;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private final CartService cartService;
    private final EmailService emailService;
    private final NotificationService notificationService;
    private final OrderService orderService;
    private final SimpMessagingTemplate messagingTemplate;
    private final PaymentService paymentService;
    private final UserService userService;
    private final ProductService productService;

    @Value("${admin.username}")
    private String adminEmail;

    public OrderConsumer(CartService cartService, EmailService emailService, NotificationService notificationService,
                         OrderService orderService, SimpMessagingTemplate messagingTemplate, PaymentService paymentService,
                         UserService userService, ProductService productService) {
        this.cartService = cartService;
        this.emailService = emailService;
        this.notificationService = notificationService;
        this.orderService = orderService;
        this.messagingTemplate = messagingTemplate;
        this.paymentService = paymentService;
        this.userService = userService;
        this.productService = productService;
    }

    @KafkaListener(
            topics = "order.created",
            groupId = "order-group",
            containerFactory = "orderCreatedKafkaListenerContainerFactory"
    )
    public void handleOrderCreated(OrderCreatedEvent event) throws MessagingException {
        System.out.println("📩 Received order.created event: " + event);
        OrderResponse order = orderService.getOrder(event.getOrderId());

        // Xoá cart
        cartService.deleteAllItemsByUser(order.getUserResponse().getUserId());

        // Gửi email invoice
        PaymentResponse.PaymentInfoResponse payment = paymentService.getPaymentById(event.getPaymentId());
        // Tạm thời chuyển qua request cho đúng hàm send mail
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId(event.getOrderId());
        paymentRequest.setAmount(payment.getAmount());
        paymentRequest.setMethod(payment.getMethod());
        paymentRequest.setTransactionId(payment.getTransactionId());
        emailService.sendInvoiceEmail(order.getUserResponse().getEmail(), paymentRequest);

        // Gửi noti
        sendNotificationToUserAndAdmin(
                "Order Created",
                "Your order #" + order.getOrderId() + " has been created successfully!",
                "New order #" + order.getOrderId() + " has been created by user " + order.getUserResponse().getEmail(),
                order.getUserResponse().getUserId(),
                "/topic/order-created"
        );
    }

    @KafkaListener(
            topics = "order.changed.status",
            groupId = "order-group",
            containerFactory = "orderChangedStatusKafkaListenerContainerFactory"
    )
    public void handleOrderChangedStatusEvent(OrderChangedStatusEvent event) {
        OrderResponse order = orderService.getOrder(event.getOrderId());
        String statusMessage = event.getOrderStatus().getMessage();

        sendNotificationToUserAndAdmin(
                "Order Status Updated",
                "Order #" + order.getOrderId() + ": " + statusMessage,
                "Order #" + order.getOrderId() + " for user " + order.getUserResponse().getEmail() + " is now: " + statusMessage,
                order.getUserResponse().getUserId(),
                "/topic/order-status-changed"
        );
    }

    @KafkaListener(
            topics = "order.cancelled",
            groupId = "order-group",
            containerFactory = "orderCancelledKafkaListenerContainerFactory"
    )
    public void handleOrderCancelledEvent(OrderCancelledEvent event) {
        OrderResponse order = orderService.getOrder(event.getOrderId());

        // Cập nhật trạng thái thanh toán
        paymentService.updatePaymentStatus(PaymentStatus.REFUNDED, event.getPaymentId());

        // Restock hàng
        order.getOrderItemResponses().forEach(orderItem -> {
            productService.increaseStock(orderItem.getProductResponse().getProductId(), orderItem.getQuantity());
        });

        sendNotificationToUserAndAdmin(
                "Order Cancelled",
                "Order #" + order.getOrderId() + ": " + OrderStatus.CANCELLED.getMessage(),
                "Order #" + order.getOrderId() + " for user " + order.getUserResponse().getEmail()
                        + " is now: " + OrderStatus.CANCELLED.getMessage(),
                order.getUserResponse().getUserId(),
                "/topic/order-cancelled"
        );
    }

    private void sendNotificationToUserAndAdmin(String title, String userMessage, String adminMessage,
                                                String userId, String topic) {
        // User
        NotificationRequest userNotification = new NotificationRequest();
        userNotification.setTitle(title);
        userNotification.setMessage(userMessage);
        userNotification.setUserId(userId);
        notificationService.createNotification(userNotification);

        // Admin
        UserResponse adminUser = userService.getUserFromEmail(adminEmail);
        NotificationRequest adminNotification = new NotificationRequest();
        adminNotification.setTitle(title);
        adminNotification.setMessage(adminMessage);
        adminNotification.setUserId(adminUser.getUserId());
        notificationService.createNotification(adminNotification);

        // Socket
        messagingTemplate.convertAndSend(topic, userNotification);
        messagingTemplate.convertAndSend(topic, adminNotification);
    }
}
