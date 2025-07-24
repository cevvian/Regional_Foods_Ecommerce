package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.ReviewMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.ReviewCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.ReviewUpdateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ReviewResponse;
import edu.ut.sales.sales_analyst.model.entities.Product;
import edu.ut.sales.sales_analyst.model.entities.Review;
import edu.ut.sales.sales_analyst.model.entities.User;
import edu.ut.sales.sales_analyst.repositories.ProductRepo;
import edu.ut.sales.sales_analyst.repositories.ReviewRepo;
import edu.ut.sales.sales_analyst.repositories.UserRepo;
import edu.ut.sales.sales_analyst.services.impl.IReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewService implements IReviewService {

    private final ReviewRepo reviewRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewRepo reviewRepo, UserRepo userRepo, ProductRepo productRepo, ReviewMapper reviewMapper) {
        this.reviewRepo = reviewRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public ReviewResponse createReview(ReviewCreateRequest request) {
        System.out.println("👉 [Service] Bắt đầu xử lý createReview");

        User user = userRepo.findByUserId(request.getUserId());
        if (user == null) {
            System.out.println("❌ Không tìm thấy user với ID: " + request.getUserId());
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        Product product = productRepo.findByProductId(request.getProductId());
        if (product == null) {
            System.out.println("❌ Không tìm thấy product với ID: " + request.getProductId());
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUser(user);
        review.setProduct(product);
        review.setCreateAt(LocalDateTime.now());
        review.setUpdateAt(LocalDateTime.now());
        System.out.println("✅ Before Save");
        reviewRepo.save(review);
        System.out.println("✅ Review saved, preparing to map to response");

        ReviewResponse response = reviewMapper.toReviewResponse(review);
        System.out.println("✅ Review mapped successfully: " + response);

        return response;
    }


    @Override
    public ReviewResponse getReview(String reviewId) {
        Review review = reviewRepo.findByReviewId(reviewId);
        if (review == null) {
            throw new AppException(ErrorCode.REVIEW_NOT_FOUND);
        }
        return reviewMapper.toReviewResponse(review);
    }

    @Override
    public Page<ReviewResponse> getAllReviews(Pageable pageable) {
        Page<Review> reviewPage = reviewRepo.findAll(pageable);
        if (reviewPage.isEmpty()) {
            throw new AppException(ErrorCode.REVIEW_LIST_EMPTY);
        }
        return reviewPage.map(reviewMapper::toReviewResponse);
    }

    @Override
    public ReviewResponse updateReview(String reviewId, ReviewUpdateRequest request) {
        Review review = reviewRepo.findByReviewId(reviewId);
        if (review == null) {
            throw new AppException(ErrorCode.REVIEW_NOT_FOUND);
        }

        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }
        if (request.getComment() != null && !request.getComment().isBlank()) {
            review.setComment(request.getComment());
        }

        if (request.getUserId() != null) {
            User user = userRepo.findByUserId(request.getUserId());
            if (user == null) {
                throw new AppException(ErrorCode.USER_NOT_FOUND);
            }
            review.setUser(user);
        }

        if (request.getProductId() != null) {
            Product product = productRepo.findByProductId(request.getProductId());
            if (product == null) {
                throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
            }
            review.setProduct(product);
        }

        review.setUpdateAt(LocalDateTime.now());
        return reviewMapper.toReviewResponse(reviewRepo.save(review));
    }

    @Override
    public Boolean deleteReview(String reviewId) {
        Review review = reviewRepo.findByReviewId(reviewId);
        if (review == null) {
            throw new AppException(ErrorCode.REVIEW_NOT_FOUND);
        }
        reviewRepo.delete(review);
        return true;
    }

    @Override
    public Page<ReviewResponse> getReviewsByProductId(String productId, Pageable pageable) {
        Product product = productRepo.findByProductId(productId);
        if (product == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        Page<Review> reviews = reviewRepo.findByProduct_ProductId(productId, pageable);
        if (reviews.isEmpty()) {
            throw new AppException(ErrorCode.REVIEW_LIST_EMPTY);
        }
        return reviews.map(reviewMapper::toReviewResponse);
    }

    @Override
    public Page<ReviewResponse> getReviewsByUserId(String userId, Pageable pageable) {
        User user = userRepo.findByUserId(userId);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        Page<Review> reviews = reviewRepo.findByUser_UserId(userId, pageable);
        if (reviews.isEmpty()) {
            throw new AppException(ErrorCode.REVIEW_LIST_EMPTY);
        }
        return reviews.map(reviewMapper::toReviewResponse);
    }

    @Override
    public Page<ReviewResponse> getReviewsByUserIdAndProductId(String userId, String productId, Pageable pageable) {

        Product product = productRepo.findByProductId(productId);
        if (product == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        User user = userRepo.findByUserId(userId);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        Page<Review> reviews = reviewRepo.findByUserAndProduct(user, product, pageable);
        if (reviews.isEmpty()) {
            throw new AppException(ErrorCode.REVIEW_LIST_EMPTY);
        }
        return reviews.map(reviewMapper::toReviewResponse);
    }

    //thong ke
    @Override
    public Double getAverageRatingByProductId(String productId) {
        Product product = productRepo.findByProductId(productId);
        if (product == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return reviewRepo.getAverageRatingByProductId(productId);
    }

    @Override
    public Long countReviewsByProductId(String productId) {
        Product product = productRepo.findByProductId(productId);
        if (product == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return reviewRepo.countReviewsByProductId(productId);
    }
}
