package edu.ut.sales.sales_analyst.entities;

import edu.ut.sales.sales_analyst.model.entities.*;
import edu.ut.sales.sales_analyst.model.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testNoArgsConstructor() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void testAllArgsConstructor() {
        String id = "u123";
        String name = "Alice";
        String password = "securepass";
        String email = "alice@example.com";
        String phone = "0123456789";
        Role role = Role.ADMIN;
        Boolean isActive = true;
        Boolean isDeleted = false;
        LocalDateTime created = LocalDateTime.now();

        List<Address> addresses = List.of();
        Cart cart = new Cart();
        List<Order> orders = List.of();
        List<Review> reviews = List.of();

        User user = new User(id, name, password, email, phone, role, isActive, isDeleted,
                created, addresses, cart, orders, reviews);

        assertEquals(id, user.getUserId());
        assertEquals(name, user.getUserName());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());
        assertEquals(phone, user.getPhone());
        assertEquals(role, user.getRole());
        assertTrue(user.getIsActive());
        assertFalse(user.getIsDeleted());
        assertEquals(created, user.getCreateAt());
        assertEquals(addresses, user.getAddresses());
        assertEquals(cart, user.getCart());
        assertEquals(orders, user.getOrders());
        assertEquals(reviews, user.getReviews());
    }

    @Test
    void testGetterSetter() {
        User user = new User();

        user.setUserId("u456");
        user.setUserName("Bob");
        user.setPassword("bobpass");
        user.setEmail("bob@example.com");
        user.setPhone("0987654321");
        user.setRole(Role.CUSTOMER);
        user.setIsActive(false);
        user.setIsDeleted(true);
        user.setCreateAt(LocalDateTime.of(2025, 8, 3, 20, 0));

        Cart cart = new Cart();
        user.setCart(cart);

        List<Address> addressList = List.of();
        user.setAddresses(addressList);

        List<Order> orderList = List.of();
        user.setOrders(orderList);

        List<Review> reviewList = List.of();
        user.setReviews(reviewList);

        assertEquals("u456", user.getUserId());
        assertEquals("Bob", user.getUserName());
        assertEquals("bobpass", user.getPassword());
        assertEquals("bob@example.com", user.getEmail());
        assertEquals("0987654321", user.getPhone());
        assertEquals(Role.CUSTOMER, user.getRole());
        assertFalse(user.getIsActive());
        assertTrue(user.getIsDeleted());
        assertEquals(LocalDateTime.of(2025, 8, 3, 20, 0), user.getCreateAt());
        assertEquals(cart, user.getCart());
        assertEquals(addressList, user.getAddresses());
        assertEquals(orderList, user.getOrders());
        assertEquals(reviewList, user.getReviews());
    }

    @Test
    void testGetAuthorities() {
        User user = new User();
        user.setRole(Role.ADMIN);

        List<? extends GrantedAuthority> authorities = (List<? extends GrantedAuthority>) user.getAuthorities();

        assertEquals(1, authorities.size());
        assertEquals("ADMIN", authorities.get(0).getAuthority());
    }
}