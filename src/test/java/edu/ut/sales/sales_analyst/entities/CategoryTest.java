package edu.ut.sales.sales_analyst.entities;

import edu.ut.sales.sales_analyst.model.entities.Category;
import edu.ut.sales.sales_analyst.model.entities.New;
import edu.ut.sales.sales_analyst.model.entities.Product;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testNoArgsConstructor() {
        Category category = new Category();
        assertNotNull(category);
        assertNotNull(category.getCreatedAt());
        assertNotNull(category.getUpdatedAt());
    }

    @Test
    void testAllArgsConstructor() {
        String categoryId = "CAT001";
        String categoryName = "Electronics";
        String description = "All electronic devices";
        LocalDateTime created = LocalDateTime.of(2023, 1, 1, 10, 0);
        LocalDateTime updated = LocalDateTime.of(2023, 1, 2, 10, 0);
        List<Product> products = new ArrayList<>();
        List<New> news = new ArrayList<>();

        Category category = new Category(categoryId, categoryName, description, created, updated, products, news);

        assertEquals(categoryId, category.getCategoryId());
        assertEquals(categoryName, category.getCategoryName());
        assertEquals(description, category.getDescription());
        assertEquals(created, category.getCreatedAt());
        assertEquals(updated, category.getUpdatedAt());
        assertEquals(products, category.getProducts());
        assertEquals(news, category.getNews());
    }

    @Test
    void testGetterSetter() {
        Category category = new Category();
        String categoryId = "CAT002";
        String categoryName = "Clothing";
        String description = "All clothes";
        LocalDateTime created = LocalDateTime.of(2023, 5, 5, 10, 0);
        LocalDateTime updated = LocalDateTime.of(2023, 6, 6, 12, 0);
        List<Product> products = new ArrayList<>();
        List<New> news = new ArrayList<>();

        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);
        category.setDescription(description);
        category.setCreatedAt(created);
        category.setUpdatedAt(updated);
        category.setProducts(products);
        category.setNews(news);

        assertEquals(categoryId, category.getCategoryId());
        assertEquals(categoryName, category.getCategoryName());
        assertEquals(description, category.getDescription());
        assertEquals(created, category.getCreatedAt());
        assertEquals(updated, category.getUpdatedAt());
        assertEquals(products, category.getProducts());
        assertEquals(news, category.getNews());
    }

    @Test
    void testPreUpdateShouldUpdateUpdatedAt() throws InterruptedException {
        Category category = new Category();
        LocalDateTime originalUpdatedAt = category.getUpdatedAt();

        Thread.sleep(5);
        category.preUpdate();

        assertTrue(category.getUpdatedAt().isAfter(originalUpdatedAt));
    }
}