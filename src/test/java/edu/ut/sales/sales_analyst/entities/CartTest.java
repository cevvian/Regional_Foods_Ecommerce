package edu.ut.sales.sales_analyst.entities;

import edu.ut.sales.sales_analyst.model.entities.Cart;
import edu.ut.sales.sales_analyst.model.entities.CartItem;
import edu.ut.sales.sales_analyst.model.entities.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    @Test
    void testNoArgsConstructor() {
        Cart cart = new Cart();
        assertNotNull(cart);
        assertNotNull(cart.getUpdatedAt()); // Should be initialized
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User();
        List<CartItem> items = new ArrayList<>();
        LocalDateTime updatedTime = LocalDateTime.of(2023, 1, 1, 10, 0);

        Cart cart = new Cart("CART001", user, updatedTime, items);

        assertEquals("CART001", cart.getCartId());
        assertEquals(user, cart.getUser());
        assertEquals(updatedTime, cart.getUpdatedAt());
        assertEquals(items, cart.getItems());
    }

    @Test
    void testGetterSetter() {
        Cart cart = new Cart();
        User user = new User();
        List<CartItem> itemList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.of(2025, 8, 3, 12, 0);

        cart.setCartId("CART002");
        cart.setUser(user);
        cart.setUpdatedAt(now);
        cart.setItems(itemList);

        assertEquals("CART002", cart.getCartId());
        assertEquals(user, cart.getUser());
        assertEquals(now, cart.getUpdatedAt());
        assertEquals(itemList, cart.getItems());
    }

    @Test
    void testPreUpdateShouldUpdateTimestamp() {
        Cart cart = new Cart();
        LocalDateTime oldTime = cart.getUpdatedAt();

        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        cart.preUpdate();

        assertTrue(cart.getUpdatedAt().isAfter(oldTime));
    }
}