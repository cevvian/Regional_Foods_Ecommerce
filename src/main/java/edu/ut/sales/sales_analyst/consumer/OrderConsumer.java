package edu.ut.sales.sales_analyst.consumer;

import edu.ut.sales.sales_analyst.model.dtos.events.OrderCreatedEvent;
import edu.ut.sales.sales_analyst.model.dtos.responses.OrderResponse;
import edu.ut.sales.sales_analyst.services.CartService;
import edu.ut.sales.sales_analyst.services.EmailService;
import edu.ut.sales.sales_analyst.services.NotificationService;
import edu.ut.sales.sales_analyst.services.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {
    private final CartService cartService;
    private final EmailService emailService;
    private final NotificationService notificationService;
    private final OrderService orderService;

    public OrderConsumer(CartService cartService, EmailService emailService, NotificationService notificationService, OrderService orderService) {
        this.cartService = cartService;
        this.emailService = emailService;
        this.notificationService = notificationService;
        this.orderService = orderService;
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
