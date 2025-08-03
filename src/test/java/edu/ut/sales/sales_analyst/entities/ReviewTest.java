package edu.ut.sales.sales_analyst.entities;

import edu.ut.sales.sales_analyst.model.entities.Product;
import edu.ut.sales.sales_analyst.model.entities.Review;
import edu.ut.sales.sales_analyst.model.entities.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    @Test
    void testNoArgsConstructor() {
        Review review = new Review();
        assertNotNull(review);
    }

    @Test
    void testAllArgsConstructor() {
        String reviewId = "R1001";
        double rating = 4.5;
        String comment = "Good product";
        LocalDateTime createAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updateAt = LocalDateTime.now().minusHours(2);
        User user = new User();
        Product product = new Product();

        Review review = new Review(reviewId, rating, comment, createAt, updateAt, user, product);

        assertEquals(reviewId, review.getReviewId());
        assertEquals(rating, review.getRating());
        assertEquals(comment, review.getComment());
        assertEquals(createAt, review.getCreateAt());
        assertEquals(updateAt, review.getUpdateAt());
        assertEquals(user, review.getUser());
        assertEquals(product, review.getProduct());
    }

    @Test
    void testGetterSetter() {
        Review review = new Review();

        review.setReviewId("R2002");
        review.setRating(3.8);
        review.setComment("Khá ổn");
        LocalDateTime now = LocalDateTime.now();
        review.setCreateAt(now);
        review.setUpdateAt(now);

        User user = new User();
        Product product = new Product();
        review.setUser(user);
        review.setProduct(product);

        assertEquals("R2002", review.getReviewId());
        assertEquals(3.8, review.getRating());
        assertEquals("Khá ổn", review.getComment());
        assertEquals(now, review.getCreateAt());
        assertEquals(now, review.getUpdateAt());
        assertEquals(user, review.getUser());
        assertEquals(product, review.getProduct());
    }

    @Test
    void testPreUpdateUpdatesTimestamp() {
        Review review = new Review();
        LocalDateTime before = review.getUpdateAt();

        try {
            Thread.sleep(10); // Đảm bảo thời gian khác biệt
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        review.preUpdate();
        assertTrue(review.getUpdateAt().isAfter(before));
    }
}