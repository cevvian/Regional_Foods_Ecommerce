package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.model.dtos.requests.ReviewCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.ReviewUpdateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.PageMeta;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.model.dtos.responses.ReviewResponse;
import edu.ut.sales.sales_analyst.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseAPI<ReviewResponse> createReview(@Valid @RequestBody ReviewCreateRequest request) {
        System.out.println("Received review create request: " + request);

        ReviewResponse response = reviewService.createReview(request);
        return new ResponseAPI<>("Create review successfully", HttpStatus.CREATED, response);
    }

    @GetMapping("/{id}")
    public ResponseAPI<ReviewResponse> getReview(@PathVariable String id) {
        ReviewResponse response = reviewService.getReview(id);
        return new ResponseAPI<>("Get review successfully", HttpStatus.OK, response);
    }

    @GetMapping
    public ResponseAPI<List<ReviewResponse>> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponse> reviewPage = reviewService.getAllReviews(pageable);

        PageMeta meta = PageMeta.builder()
                .page(reviewPage.getNumber())
                .size(reviewPage.getSize())
                .totalElements(reviewPage.getTotalElements())
                .totalPages(reviewPage.getTotalPages())
                .last(reviewPage.isLast())
                .build();

        return new ResponseAPI<>("Get all reviews successfully", HttpStatus.OK, reviewPage.getContent(), meta);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseAPI<ReviewResponse> updateReview(
            @PathVariable String id,
            @Valid @RequestBody ReviewUpdateRequest request
    ) {
        ReviewResponse response = reviewService.updateReview(id, request);
        return new ResponseAPI<>("Update review successfully", HttpStatus.OK, response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseAPI<Boolean> deleteReview(@PathVariable String id) {
        boolean deleted = reviewService.deleteReview(id);
        if (deleted) {
            return new ResponseAPI<>("Delete review successfully", HttpStatus.OK, true);
        } else {
            return new ResponseAPI<>("Delete review failed", HttpStatus.NOT_FOUND, null);
        }
    }

    @GetMapping("/by-product")
    public ResponseAPI<List<ReviewResponse>> getReviewsByProduct(
            @RequestParam String productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponse> reviewPage = reviewService.getReviewsByProductId(productId, pageable);

        PageMeta meta = PageMeta.builder()
                .page(reviewPage.getNumber())
                .size(reviewPage.getSize())
                .totalElements(reviewPage.getTotalElements())
                .totalPages(reviewPage.getTotalPages())
                .last(reviewPage.isLast())
                .build();

        return new ResponseAPI<>("Get reviews by product successfully", HttpStatus.OK, reviewPage.getContent(), meta);
    }


    @GetMapping("/by-user")
    public ResponseAPI<List<ReviewResponse>> getReviewsByUser(
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponse> reviewPage = reviewService.getReviewsByUserId(userId, pageable);

        PageMeta meta = PageMeta.builder()
                .page(reviewPage.getNumber())
                .size(reviewPage.getSize())
                .totalElements(reviewPage.getTotalElements())
                .totalPages(reviewPage.getTotalPages())
                .last(reviewPage.isLast())
                .build();

        return new ResponseAPI<>("Get reviews by user successfully", HttpStatus.OK, reviewPage.getContent(), meta);
    }

    @GetMapping("/by-product-and-user")
    public ResponseAPI<List<ReviewResponse>> getReviewsByProductAndUser(
            @RequestParam String productId,
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponse> reviewPage = reviewService.getReviewsByUserIdAndProductId(userId, productId, pageable);

        PageMeta meta = PageMeta.builder()
                .page(reviewPage.getNumber())
                .size(reviewPage.getSize())
                .totalElements(reviewPage.getTotalElements())
                .totalPages(reviewPage.getTotalPages())
                .last(reviewPage.isLast())
                .build();

        return new ResponseAPI<>("Get reviews by product and user successfully", HttpStatus.OK, reviewPage.getContent(), meta);
    }

}