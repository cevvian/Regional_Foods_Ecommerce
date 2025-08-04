package edu.ut.sales.sales_analyst.model.dtos.requests;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
public class ReviewUpdateRequest {

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating must be at most 5.0")
    private Double rating;

    private String comment;

//    private String userId;
//
//    private String productId;
}
