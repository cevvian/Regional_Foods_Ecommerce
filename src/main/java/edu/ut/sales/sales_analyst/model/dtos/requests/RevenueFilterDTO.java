package edu.ut.sales.sales_analyst.model.dtos.requests;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RevenueFilterDTO {

    @Min(value = 2000, message = "Năm phải lớn hơn hoặc bằng 2000")
    @Max(value = 2100, message = "Năm phải nhỏ hơn hoặc bằng 2100")
    @Schema(description = "Năm cần thống kê (có thể để trống)", example = "2025", required = false)
    private Integer year;

    @Min(value = 1, message = "Tháng phải từ 1 đến 12")
    @Max(value = 12, message = "Tháng phải từ 1 đến 12")
    @Schema(description = "Tháng cần thống kê (nếu có)", example = "7", required = false)
    private Integer month;

    @Schema(description = "ID sản phẩm để lọc (nếu có)", example = "prod-123", required = false)
    private String productId;

    @Schema(description = "ID vùng để lọc (nếu có)", example = "region-001", required = false)
    private String regionId;
}

