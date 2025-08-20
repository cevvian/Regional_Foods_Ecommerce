package edu.ut.sales.sales_analyst.model.dtos.requests;

import edu.ut.sales.sales_analyst.model.enums.NewType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class NewUpdateRequest {

    @Schema(description = "New title", example = "Updated Sale Event")
    private String title;

    @Schema(description = "Updated content", example = "We're extending our sale!")
    private String content;

    @Schema(description = "New category ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private String categoryId;

    @Schema(description = "Optional new images. Replaces existing images if provided", type = "array", implementation = MultipartFile.class)
    private List<ImageOfNewCreateRequest> images;

    @Schema(description = "Type of news", example = "PROMOTION")
    private NewType type;
}
