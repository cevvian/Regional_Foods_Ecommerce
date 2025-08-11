package edu.ut.sales.sales_analyst.consumer;

import edu.ut.sales.sales_analyst.model.dtos.events.OrderCreatedEvent;
import edu.ut.sales.sales_analyst.model.dtos.responses.OrderResponse;
import edu.ut.sales.sales_analyst.services.*;
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

    public OrderConsumer(CartService cartService, EmailService emailService, NotificationService notificationService, OrderService orderService, SimpMessagingTemplate messagingTemplate, PaymentService paymentService) {
        this.cartService = cartService;
        this.emailService = emailService;
        this.notificationService = notificationService;
        this.orderService = orderService;
        this.messagingTemplate = messagingTemplate;
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "order.created", groupId = "order-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        OrderResponse order = orderService.getOrder(event.getOrderId());
        //Xoá cartItem

        // Gửi email confirm
        System.out.println("Email sent to " + order.getUserResponse().getEmail());
        // Push noti
        System.out.println("Push noti: Order created " + event.getOrderId());
    }
}
