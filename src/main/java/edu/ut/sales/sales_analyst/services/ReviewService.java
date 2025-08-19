package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.ReviewMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.ReviewCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.ReviewUpdateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ReviewResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.ReviewStatsDTO;
import edu.ut.sales.sales_analyst.model.entities.Category;
import edu.ut.sales.sales_analyst.model.entities.Product;
import edu.ut.sales.sales_analyst.model.entities.Review;
import edu.ut.sales.sales_analyst.model.entities.User;
import edu.ut.sales.sales_analyst.repositories.*;
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
    private final UserService userService;
    private final OrderRepo orderRepo;
    private final CategoryRepo categoryRepo;

    public ReviewService(ReviewRepo reviewRepo, UserRepo userRepo, ProductRepo productRepo,
                         ReviewMapper reviewMapper, UserService userService, OrderRepo orderRepo,
                         CategoryRepo categoryRepo) {
        this.reviewRepo = reviewRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.reviewMapper = reviewMapper;
        this.userService = userService;
        this.orderRepo = orderRepo;
        this.categoryRepo = categoryRepo;
    }

    private void updateProductRating(Product product) {
        Double averageRating = reviewRepo.getAverageRatingByProductId(product.getProductId());
        product.setRating(averageRating != null ? averageRating : 0.0);
        product.setUpdateAt(LocalDateTime.now());
        productRepo.save(product);
    }

    @Override
    public ReviewResponse createReview(ReviewCreateRequest request) {
        User user = userRepo.findByUserId(userService.getCurrentUser().getUserId());
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        Product product = productRepo.findByProductId(request.getProductId());
        if (product == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

//        boolean hasPurchased = orderRepo.existsCompletedOrderByUserIdAndProductId(user.getUserId(), product.getProductId());
//        if (!hasPurchased) {
//            throw new AppException(ErrorCode.UNAUTHORIZED_REVIEW);
//        }


        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUser(user);
        review.setProduct(product);
        review.setCreateAt(LocalDateTime.now());
        review.setUpdateAt(LocalDateTime.now());

        reviewRepo.save(review);
        updateProductRating(product);  // Update rating sau khi tạo

        return reviewMapper.toReviewResponse(review);
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

//        Product oldProduct = review.getProduct();
//
//        User user = request.getUserId() != null
//                ? userRepo.findByUserId(request.getUserId())
//                : review.getUser();
//
//        if (user == null) {
//            throw new AppException(ErrorCode.USER_NOT_FOUND);
//        }
//
//        Product product = request.getProductId() != null
//                ? productRepo.findByProductId(request.getProductId())
//                : review.getProduct();
//
//        if (product == null) {
//            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
//        }
//
//        boolean hasPurchased = orderRepo.existsCompletedOrderByUserIdAndProductId(user.getUserId(), product.getProductId());
//        if (!hasPurchased) {
//            throw new AppException(ErrorCode.UNAUTHORIZED_REVIEW);
//        }
//
//        if (request.getRating() != null) {
//            review.setRating(request.getRating());
//        }

        if (request.getComment() != null && !request.getComment().isBlank()) {
            review.setComment(request.getComment());
        }

//        review.setUser(user);
//        review.setProduct(product);
        review.setUpdateAt(LocalDateTime.now());

        Review updated = reviewRepo.save(review);

        updateProductRating(updated.getProduct());
//        if (!oldProduct.getProductId().equals(updated.getProduct().getProductId())) {
//            updateProductRating(oldProduct);
//        }

        return reviewMapper.toReviewResponse(updated);
    }


    @Override
    public Boolean deleteReview(String reviewId) {
        Review review = reviewRepo.findByReviewId(reviewId);
        if (review == null) {
            throw new AppException(ErrorCode.REVIEW_NOT_FOUND);
        }
        Product product = review.getProduct();
        reviewRepo.delete(review);
        updateProductRating(product); // Update rating sau khi xóa
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

    @Override
    public Page<ReviewResponse> getReviewsByRatingAndCategoryId(Double rating, String categoryId, Pageable pageable) {
        // Nếu categoryId không null, kiểm tra tồn tại
        if (categoryId != null) {
            Category category = categoryRepo.findByCategoryId(categoryId);
            if (category == null) {
                throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
            }
        }

        // Gọi repo với rating và categoryId có thể null
        Page<Review> reviews = reviewRepo.findByRatingAndCategory(
                rating,       // có thể null
                categoryId,   // có thể null
                pageable
        );

        if (reviews.isEmpty()) {
            throw new AppException(ErrorCode.REVIEW_LIST_EMPTY);
        }

        return reviews.map(reviewMapper::toReviewResponse);
    }


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

    @Override
    public ReviewStatsDTO getGlobalReviewStats() {
        long total = reviewRepo.count();

        double avg = reviewRepo.findAll().stream()
                .mapToDouble(r -> r.getRating())
                .average()
                .orElse(0.0);

        return new ReviewStatsDTO(
                total,
                Math.round(avg * 10.0) / 10.0,
                reviewRepo.countByRating(5),
                reviewRepo.countByRating(4),
                reviewRepo.countByRating(3),
                reviewRepo.countByRating(2),
                reviewRepo.countByRating(1),
                reviewRepo.countDistinctCustomers()
        );
    }

}
