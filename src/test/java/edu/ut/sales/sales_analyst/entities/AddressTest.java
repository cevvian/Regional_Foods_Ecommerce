package edu.ut.sales.sales_analyst.entities;

import edu.ut.sales.sales_analyst.model.entities.Address;
import edu.ut.sales.sales_analyst.model.entities.Order;
import edu.ut.sales.sales_analyst.model.entities.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    void testNoArgsConstructor() {
        Address address = new Address();
        assertNotNull(address);
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User();
        List<Order> orders = new ArrayList<>();

        Address address = new Address(
                "ADDR001",
                user,
                "123 Street",
                "Hanoi",
                "0123456789",
                true,
                orders
        );

        assertEquals("ADDR001", address.getAddressId());
        assertEquals(user, address.getUser());
        assertEquals("123 Street", address.getAddressLine());
        assertEquals("Hanoi", address.getProvince());
        assertEquals("0123456789", address.getPhone());
        assertTrue(address.getIsDefault());
        assertEquals(orders, address.getOrders());
    }

    @Test
    void testSetterGetter() {
        Address address = new Address();

        User user = new User();
        List<Order> orders = new ArrayList<>();

        address.setAddressId("ADDR002");
        address.setUser(user);
        address.setAddressLine("456 Avenue");
        address.setProvince("Ho Chi Minh");
        address.setPhone("0987654321");
        address.setIsDefault(false);
        address.setOrders(orders);

        assertEquals("ADDR002", address.getAddressId());
        assertEquals(user, address.getUser());
        assertEquals("456 Avenue", address.getAddressLine());
        assertEquals("Ho Chi Minh", address.getProvince());
        assertEquals("0987654321", address.getPhone());
        assertFalse(address.getIsDefault());
        assertEquals(orders, address.getOrders());
    }
}