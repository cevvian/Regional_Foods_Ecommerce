package edu.ut.sales.sales_analyst.model.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest {
    @NotBlank(message = "Product's name can not be blank")
    private String productName;

    @NotBlank(message = "Category's id can not be blank")
    private String categoryId;

    @NotBlank(message = "Region's id can not be blank")
    private String regionId;

    @NotBlank(message = "Product's description can not be blank")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    private int stockQuantity;

    @NotEmpty(message = "Please upload at least one image")
    @Size(max = 5, message = "You can upload up to 5 images")
    @Schema(description = "Upload up to 5 images", type = "array", implementation = MultipartFile.class)
    private List<ImageProductCreationRequest> images;
}
