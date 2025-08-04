package edu.ut.sales.sales_analyst.model.dtos.responses;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ReviewResponse {
    private String reviewId;
    private double rating;
    private String comment;
    private Date createAt;
    private Date updateAt;
    private UserResponse user;
    private ProductResponse product;
}
