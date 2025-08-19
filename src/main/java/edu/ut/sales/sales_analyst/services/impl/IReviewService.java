package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.requests.ReviewCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.ReviewUpdateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ReviewResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.ReviewStatsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IReviewService {
    ReviewResponse createReview(ReviewCreateRequest request);
    ReviewResponse getReview(String reviewId);
    Page<ReviewResponse> getAllReviews(Pageable pageable);
    ReviewResponse updateReview(String reviewId, ReviewUpdateRequest request);
    Boolean deleteReview(String reviewId);

    Page<ReviewResponse> getReviewsByProductId(String productId, Pageable pageable);
    Page<ReviewResponse> getReviewsByUserId(String userId, Pageable pageable);
    Page<ReviewResponse> getReviewsByUserIdAndProductId(String userId, String productId, Pageable pageable);
    Page<ReviewResponse> getReviewsByRatingAndCategoryId(Double rating, String categoryId, Pageable pageable);

    Double getAverageRatingByProductId(String productId);
    Long countReviewsByProductId(String productId);
    ReviewStatsDTO getGlobalReviewStats();
}
