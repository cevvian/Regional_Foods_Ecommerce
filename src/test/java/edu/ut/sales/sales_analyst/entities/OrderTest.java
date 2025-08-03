package edu.ut.sales.sales_analyst.entities;

import edu.ut.sales.sales_analyst.model.entities.Address;
import edu.ut.sales.sales_analyst.model.entities.Order;
import edu.ut.sales.sales_analyst.model.entities.OrderItem;
import edu.ut.sales.sales_analyst.model.entities.User;
import edu.ut.sales.sales_analyst.model.enums.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testNoArgsConstructor() {
        Order order = new Order();
        assertNotNull(order);
    }

    @Test
    void testAllArgsConstructor() {
        String orderId = "ORD001";
        User user = new User();
        BigDecimal totalAmount = BigDecimal.valueOf(299.99);
        OrderStatus status = OrderStatus.PENDING;
        LocalDateTime orderDate = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 1, 2, 12, 0);
        boolean isActive = true;
        Address address = new Address();
        OrderItem item = new OrderItem();

        Order order = new Order(
                orderId,
                user,
                totalAmount,
                status,
                orderDate,
                updatedAt,
                isActive,
                address,
                Collections.singletonList(item)
        );

        assertEquals(orderId, order.getOrderId());
        assertEquals(user, order.getUser());
        assertEquals(totalAmount, order.getTotalAmount());
        assertEquals(status, order.getStatus());
        assertEquals(orderDate, order.getOrderDate());
        assertEquals(updatedAt, order.getUpdatedAt());
        assertTrue(order.isActive());
        assertEquals(address, order.getAddress());
        assertEquals(1, order.getOrderItems().size());
    }

    @Test
    void testGetterSetter() {
        Order order = new Order();

        User user = new User();
        BigDecimal totalAmount = BigDecimal.valueOf(150.50);
        OrderStatus status = OrderStatus.SHIPPED;
        LocalDateTime orderDate = LocalDateTime.of(2025, 5, 5, 15, 30);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 5, 6, 9, 0);
        Address address = new Address();
        OrderItem item = new OrderItem();

        order.setOrderId("ORD002");
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setStatus(status);
        order.setOrderDate(orderDate);
        order.setUpdatedAt(updatedAt);
        order.setActive(false);
        order.setAddress(address);
        order.setOrderItems(Collections.singletonList(item));

        assertEquals("ORD002", order.getOrderId());
        assertEquals(user, order.getUser());
        assertEquals(totalAmount, order.getTotalAmount());
        assertEquals(status, order.getStatus());
        assertEquals(orderDate, order.getOrderDate());
        assertEquals(updatedAt, order.getUpdatedAt());
        assertFalse(order.isActive());
        assertEquals(address, order.getAddress());
        assertEquals(1, order.getOrderItems().size());
    }

    @Test
    void testDefaultValues() {
        Order order = new Order();

        assertNotNull(order.getOrderDate());
        assertNotNull(order.getUpdatedAt());
        assertTrue(order.isActive());

        assertTrue(order.getOrderDate().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(order.getUpdatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testPreUpdate() {
        Order order = new Order();
        LocalDateTime beforeUpdate = order.getUpdatedAt();

        // Giả lập gọi hàm preUpdate
        order.preUpdate();
        LocalDateTime afterUpdate = order.getUpdatedAt();

        assertTrue(afterUpdate.isAfter(beforeUpdate) || afterUpdate.equals(beforeUpdate));
    }
}