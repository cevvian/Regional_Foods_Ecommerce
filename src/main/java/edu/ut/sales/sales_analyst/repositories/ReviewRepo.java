package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Integer> {
    Page<Review> findByProduct_ProductId(String productId, Pageable pageable);

    Page<Review> findByUser_UserId(String userId, Pageable pageable);

    Page<Review> findByUser_UserIdAndProduct_ProductId(String userId, String productId, Pageable pageable);

    Review findByReviewId(String reviewId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.productId = :productId")
    Double getAverageRatingByProductId(String productId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.productId = :productId")
    Long countReviewsByProductId(String productId);
}
