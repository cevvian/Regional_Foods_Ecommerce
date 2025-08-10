package edu.ut.sales.sales_analyst.consumer;

import edu.ut.sales.sales_analyst.model.dtos.events.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    @KafkaListener(topics = "order.created", groupId = "order-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        // Gửi email confirm
        System.out.println("Email sent to " + event.getEmail());
        // Push noti
        System.out.println("Push noti: Order created " + event.getOrderId());
    }
}
