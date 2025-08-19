package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Product;
import edu.ut.sales.sales_analyst.model.entities.Review;
import edu.ut.sales.sales_analyst.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Integer> {
    Page<Review> findByProduct_ProductId(String productId, Pageable pageable);

    Page<Review> findByUser_UserId(String userId, Pageable pageable);

    Page<Review> findByUserAndProduct(User user, Product product, Pageable pageable);

    @Query("""
    SELECT r FROM Review r
    WHERE (:rating IS NULL OR r.rating = :rating)
      AND (:categoryId IS NULL OR r.product.category.categoryId = :categoryId)
""")
    Page<Review> findByRatingAndCategory(
            @Param("rating") Double rating,
            @Param("categoryId") String categoryId,
            Pageable pageable
    );

    Review findByReviewId(String reviewId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.productId = :productId")
    Double getAverageRatingByProductId(String productId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.productId = :productId")
    Long countReviewsByProductId(String productId);


    // Đếm theo rating
    long countByRating(int rating);

    // Đếm tổng khách hàng unique
    @Query("SELECT COUNT(DISTINCT r.user.email) FROM Review r")
    long countDistinctCustomers();
}
