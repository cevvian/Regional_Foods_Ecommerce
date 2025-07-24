package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.model.dtos.requests.ReviewCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.ReviewUpdateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.model.dtos.responses.ReviewResponse;
import edu.ut.sales.sales_analyst.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseAPI<ReviewResponse> createReview(@Valid @RequestBody ReviewCreateRequest request) {
        try {
            System.out.println("Received review create request: " + request);

            ReviewResponse response = reviewService.createReview(request);
            return new ResponseAPI<>("Create review successfully", HttpStatus.CREATED, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/{id}")
    public ResponseAPI<ReviewResponse> getReview(@PathVariable String id) {
        try {
            ReviewResponse response = reviewService.getReview(id);
            return new ResponseAPI<>("Get review successfully", HttpStatus.OK, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }

    @GetMapping
    public ResponseAPI<Page<ReviewResponse>> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ReviewResponse> response = reviewService.getAllReviews(pageable);
            return new ResponseAPI<>("Get all reviews successfully", HttpStatus.OK, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PutMapping("/{id}")
    public ResponseAPI<ReviewResponse> updateReview(
            @PathVariable String id,
            @Valid @RequestBody ReviewUpdateRequest request
    ) {
        try {
            ReviewResponse response = reviewService.updateReview(id, request);
            return new ResponseAPI<>("Update review successfully", HttpStatus.OK, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseAPI<Boolean> deleteReview(@PathVariable String id) {
        try {
            boolean deleted = reviewService.deleteReview(id);
            if (deleted) {
                return new ResponseAPI<>("Delete review successfully", HttpStatus.OK, true);
            } else {
                return new ResponseAPI<>("Delete review failed", HttpStatus.NOT_FOUND, null);
            }
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/by-product")
    public ResponseAPI<Page<ReviewResponse>> getReviewsByProduct(
            @RequestParam String productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ReviewResponse> response = reviewService.getReviewsByProductId(productId, pageable);
            return new ResponseAPI<>("Get reviews by product successfully", HttpStatus.OK, response);
        }
        catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/by-user")
    public ResponseAPI<Page<ReviewResponse>> getReviewsByUser(
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ReviewResponse> response = reviewService.getReviewsByUserId(userId, pageable);
            return new ResponseAPI<>("Get reviews by user successfully", HttpStatus.OK, response);
        }
        catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/by-product-and-user")
    public ResponseAPI<Page<ReviewResponse>> getReviewsByProductAndUser(
            @RequestParam String productId,
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ReviewResponse> response = reviewService.getReviewsByUserIdAndProductId(productId, userId, pageable);
            return new ResponseAPI<>("Get reviews by product and user successfully", HttpStatus.OK, response);
        }
        catch (AppException e) {
            Pageable pageable = PageRequest.of(page, size);
            Page<ReviewResponse> response = reviewService.getReviewsByUserIdAndProductId(productId, userId, pageable);
            return new ResponseAPI<>("Get reviews by product and user successfully", HttpStatus.OK, response);
        }
    }
}
