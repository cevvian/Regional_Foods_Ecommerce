package edu.ut.sales.sales_analyst.consumer;

import edu.ut.sales.sales_analyst.model.dtos.events.*;
import edu.ut.sales.sales_analyst.model.dtos.requests.*;
import edu.ut.sales.sales_analyst.model.dtos.responses.*;
import edu.ut.sales.sales_analyst.model.enums.OrderStatus;
import edu.ut.sales.sales_analyst.model.enums.PaymentStatus;
import edu.ut.sales.sales_analyst.services.*;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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
        log.info(" Payment ID: {}", event.getPaymentId());
        PaymentResponse.PaymentInfoResponse payment = paymentService.getPaymentById(event.getPaymentId());
        // Tạm thời chuyển qua request cho đúng hàm send mail
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId(event.getOrderId());
        paymentRequest.setAmount(payment.getAmount());
        paymentRequest.setMethod(payment.getMethod());
        paymentRequest.setTransactionId(payment.getTransactionId());
        log.info("📧 Sending invoice email to {} | orderId={} | amount={} | method={} | txn={}",
                order.getUserResponse().getEmail(),
                paymentRequest.getOrderId(),
                paymentRequest.getAmount(),
                paymentRequest.getMethod(),
                paymentRequest.getTransactionId()
        );
        emailService.sendInvoiceEmail(order.getUserResponse().getEmail(), paymentRequest);

        log.info("✅ Invoice email sent successfully to {}", order.getUserResponse().getEmail());
        log.info("User ID: {}", order.getUserResponse().getUserId());
        // Gửi noti
        sendNotificationToUserAndAdmin(
                "Đơn hàng mới",
                "Đơn hàng số #" + order.getOrderId() + " của bạn đã được tạo thành công!",
                "Đơn hàng mới #" + order.getOrderId() + " đã được tạo bởi người dùng " + order.getUserResponse().getEmail(),
                order.getUserResponse().getUserId()
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
                "Cập nhật trạng thái đơn hàng",
                "Đơn hàng số #" + order.getOrderId() + ": " + statusMessage,
                "Đơn hàng số #" + order.getOrderId() + " của người dùng " + order.getUserResponse().getEmail() + " hiện tại là: " + statusMessage,
                order.getUserResponse().getUserId()
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
                "Hủy Đơn",
                "Đơn hàng số #" + order.getOrderId() + ": " + OrderStatus.CANCELLED.getMessage(),
                "Đơn hàng số #" + order.getOrderId() + " của người dùng " + order.getUserResponse().getEmail() + " hiện tại là: " + OrderStatus.CANCELLED.getMessage(),
                order.getUserResponse().getUserId()
        );
    }

    private void sendNotificationToUserAndAdmin(String title, String userMessage, String adminMessage, String userId) {
        System.out.println("Function sendNotificationToUserAndAdmin called");
        NotificationRequest userNotification = new NotificationRequest(userId, title, userMessage);
        NotificationResponse notification = notificationService.createNotification(userNotification);
        log.info("Create notification successfully: {}", notification);
        messagingTemplate.convertAndSend("/queue/notifications-" + userId, userNotification);

        UserResponse adminUser = userService.getUserFromEmail(adminEmail);
        log.info("Admin info: {}", adminUser);
        if (adminUser != null) {
            // Nếu NotificationRequest(userId, title, content)
            NotificationRequest adminNotification = new NotificationRequest(adminUser.getUserId(), title, adminMessage);
            notificationService.createNotification(adminNotification);
            messagingTemplate.convertAndSend("/queue/notifications-" + adminUser.getUserId(), adminNotification);
        } else {
            log.warn("Không tìm thấy tài khoản quản trị viên với email {}. Không thể gửi thông báo cho quản trị viên.", adminEmail);
        }
    }
}
