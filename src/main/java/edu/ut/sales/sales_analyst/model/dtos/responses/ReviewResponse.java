package edu.ut.sales.sales_analyst.model.dtos.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponse {
    private String reviewId;
    private double rating;
    private String comment;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String userId;
    private String productId;
}
