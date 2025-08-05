package edu.ut.sales.sales_analyst.model.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProductFilterRequest {
    @Schema(description = "ID danh mục sản phẩm", example = "cat123")
    private String categoryId;

    @Schema(description = "ID khu vực vùng miền", example = "region456")
    private String regionId;

    @Schema(description = "Giá tối thiểu", example = "10.0")
    private Double minPrice;

    @Schema(description = "Giá tối đa", example = "100.0")
    private Double maxPrice;

    @Schema(description = "Số lượng tồn kho tối thiểu", example = "5")
    private Integer minStock;

    @Schema(description = "Xếp hạng tối thiểu", example = "4.5")
    private Double minRating;
}
