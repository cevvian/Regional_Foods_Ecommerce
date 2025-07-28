package edu.ut.sales.sales_analyst.model.dtos.requests;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewCreateRequest {

    @NotNull(message = "rating is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating must be at most 5.0")
    private Double rating;

    @NotBlank(message = "comment is required")
    private String comment;

    @NotBlank(message = "userId is required")
    private String userId;

    @NotBlank(message = "productId is required")
    private String productId;
}
