package edu.ut.sales.sales_analyst.model.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private Double rating;
    private int stockQuantity;
    private CategoryResponse category;
    private RegionResponse region;
}
