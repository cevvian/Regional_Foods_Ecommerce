package edu.ut.sales.sales_analyst.entities;

import edu.ut.sales.sales_analyst.model.entities.Order;
import edu.ut.sales.sales_analyst.model.entities.OrderItem;
import edu.ut.sales.sales_analyst.model.entities.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void testNoArgsConstructor() {
        OrderItem item = new OrderItem();
        assertNotNull(item);
    }

    @Test
    void testAllArgsConstructor() {
        String itemId = "ITEM001";
        int quantity = 3;
        BigDecimal unitPrice = BigDecimal.valueOf(199.99);
        Order order = new Order();
        Product product = new Product();

        OrderItem item = new OrderItem(itemId, quantity, unitPrice, order, product);

        assertEquals(itemId, item.getOrderItemId());
        assertEquals(quantity, item.getQuantity());
        assertEquals(unitPrice, item.getUnitPrice());
        assertEquals(order, item.getOrder());
        assertEquals(product, item.getProduct());
    }

    @Test
    void testGetterSetter() {
        OrderItem item = new OrderItem();

        item.setOrderItemId("ITEM002");
        item.setQuantity(5);
        item.setUnitPrice(BigDecimal.valueOf(49.95));

        Order order = new Order();
        Product product = new Product();

        item.setOrder(order);
        item.setProduct(product);

        assertEquals("ITEM002", item.getOrderItemId());
        assertEquals(5, item.getQuantity());
        assertEquals(BigDecimal.valueOf(49.95), item.getUnitPrice());
        assertEquals(order, item.getOrder());
        assertEquals(product, item.getProduct());
    }
}