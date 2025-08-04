package edu.ut.sales.sales_analyst.entities;


import edu.ut.sales.sales_analyst.model.entities.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testNoArgsConstructor() {
        Product product = new Product();
        assertNotNull(product);
    }

    @Test
    void testAllArgsConstructor() {
        String productId = "P001";
        String productName = "Sản phẩm A";
        String description = "Mô tả sản phẩm";
        BigDecimal price = BigDecimal.valueOf(199000);
        int stockQuantity = 100;
        Double rating = 4.5;
        LocalDateTime createAt = LocalDateTime.of(2025, 8, 1, 9, 0);
        LocalDateTime updateAt = LocalDateTime.of(2025, 8, 1, 10, 0);
        Category category = new Category();
        Region region = new Region();
        List<ImageProduct> images = List.of(new ImageProduct());
        List<Review> reviews = List.of(new Review());
        boolean isDeleted = false;

        Product product = new Product(productId, productName, description, price, stockQuantity, rating,
                createAt, updateAt, category, region, images, reviews, isDeleted);

        assertEquals(productId, product.getProductId());
        assertEquals(productName, product.getProductName());
        assertEquals(description, product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(stockQuantity, product.getStockQuantity());
        assertEquals(rating, product.getRating());
        assertEquals(createAt, product.getCreateAt());
        assertEquals(updateAt, product.getUpdateAt());
        assertEquals(category, product.getCategory());
        assertEquals(region, product.getRegion());
        assertEquals(images, product.getImages());
        assertEquals(reviews, product.getReviews());
        assertFalse(product.isDeleted());
    }

    @Test
    void testGetterSetter() {
        Product product = new Product();

        product.setProductId("P002");
        product.setProductName("Laptop");
        product.setDescription("High-end laptop");
        product.setPrice(BigDecimal.valueOf(25000000));
        product.setStockQuantity(20);
        product.setRating(4.8);
        LocalDateTime now = LocalDateTime.now();
        product.setCreateAt(now);
        product.setUpdateAt(now);

        Category category = new Category();
        Region region = new Region();
        product.setCategory(category);
        product.setRegion(region);

        List<ImageProduct> images = List.of(new ImageProduct());
        List<Review> reviews = List.of(new Review());
        product.setImages(images);
        product.setReviews(reviews);
        product.setDeleted(true);

        assertEquals("P002", product.getProductId());
        assertEquals("Laptop", product.getProductName());
        assertEquals("High-end laptop", product.getDescription());
        assertEquals(BigDecimal.valueOf(25000000), product.getPrice());
        assertEquals(20, product.getStockQuantity());
        assertEquals(4.8, product.getRating());
        assertEquals(now, product.getCreateAt());
        assertEquals(now, product.getUpdateAt());
        assertEquals(category, product.getCategory());
        assertEquals(region, product.getRegion());
        assertEquals(images, product.getImages());
        assertEquals(reviews, product.getReviews());
        assertTrue(product.isDeleted());
    }

    @Test
    void testPreUpdate() {
        Product product = new Product();
        product.setUpdateAt(null);
        product.preUpdate();
        assertNotNull(product.getUpdateAt());
    }
}