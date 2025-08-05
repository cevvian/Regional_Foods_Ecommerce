package edu.ut.sales.sales_analyst.entities;

import edu.ut.sales.sales_analyst.model.entities.Cart;
import edu.ut.sales.sales_analyst.model.entities.CartItem;
import edu.ut.sales.sales_analyst.model.entities.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CartItemTest {

    @Test
    void testNoArgsConstructor() {
        CartItem item = new CartItem();
        assertNotNull(item);
    }

    @Test
    void testAllArgsConstructor() {
        Cart cart = new Cart();
        cart.setCartId("CART123");

        Product product = new Product();
        product.setProductId("PROD456");

        CartItem item = new CartItem("ITEM789", 5, cart, product);

        assertEquals("ITEM789", item.getCartItemId());
        assertEquals(5, item.getQuantity());
        assertEquals("CART123", item.getCart().getCartId());
        assertEquals("PROD456", item.getProduct().getProductId());
    }

    @Test
    void testGetterSetter() {
        CartItem item = new CartItem();

        Cart cart = new Cart();
        cart.setCartId("CART999");

        Product product = new Product();
        product.setProductId("PROD999");

        item.setCartItemId("ITEM999");
        item.setQuantity(10);
        item.setCart(cart);
        item.setProduct(product);

        assertEquals("ITEM999", item.getCartItemId());
        assertEquals(10, item.getQuantity());
        assertEquals("CART999", item.getCart().getCartId());
        assertEquals("PROD999", item.getProduct().getProductId());
    }
}