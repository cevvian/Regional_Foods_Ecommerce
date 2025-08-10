package edu.ut.sales.sales_analyst.producer;

import edu.ut.sales.sales_analyst.model.dtos.events.OrderCreatedEvent;
import edu.ut.sales.sales_analyst.model.dtos.events.PasswordChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPasswordChangedEvent(PasswordChangedEvent event) {
        log.info("[KAFKA] sending {}", event);
        try {
            var res = kafkaTemplate.send("user.password.changed", event)
                    .get(5, java.util.concurrent.TimeUnit.SECONDS);
            log.info("[KAFKA] sent topic={}, partition={}, offset={}",
                    res.getRecordMetadata().topic(),
                    res.getRecordMetadata().partition(),
                    res.getRecordMetadata().offset());
        } catch (Exception e) {
            log.error("[KAFKA] send failed", e);
        }
    }

    public void sendOrderCreatedEvent(OrderCreatedEvent event) {
        kafkaTemplate.send("order.created", event);
    }
}
