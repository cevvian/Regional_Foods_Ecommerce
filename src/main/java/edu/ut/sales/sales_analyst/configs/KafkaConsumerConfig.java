package edu.ut.sales.sales_analyst.configs;

import edu.ut.sales.sales_analyst.model.dtos.events.OrderCancelledEvent;
import edu.ut.sales.sales_analyst.model.dtos.events.OrderChangedStatusEvent;
import edu.ut.sales.sales_analyst.model.dtos.events.OrderCreatedEvent;
import edu.ut.sales.sales_analyst.model.dtos.events.PasswordChangedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    private Map<String, Object> baseConfig(String groupId) {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return config;
    }

    private <T> ConsumerFactory<String, T> consumerFactory(Class<T> type, String groupId) {
        return new DefaultKafkaConsumerFactory<>(
                baseConfig(groupId),
                new StringDeserializer(),
                new JsonDeserializer<>(type)
        );
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T> kafkaListenerFactory(Class<T> type, String groupId) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(type, groupId));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PasswordChangedEvent> passwordChangedKafkaListenerContainerFactory() {
        return kafkaListenerFactory(PasswordChangedEvent.class, "notification-group");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> orderCreatedKafkaListenerContainerFactory() {
        return kafkaListenerFactory(OrderCreatedEvent.class, "order-group");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderChangedStatusEvent> orderChangedStatusKafkaListenerContainerFactory() {
        return kafkaListenerFactory(OrderChangedStatusEvent.class, "order-group");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCancelledEvent> orderCancelledKafkaListenerContainerFactory() {
        return kafkaListenerFactory(OrderCancelledEvent.class, "order-group");
    }

}