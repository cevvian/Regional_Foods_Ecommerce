package edu.ut.sales.sales_analyst.model.dtos.requests;

import edu.ut.sales.sales_analyst.model.enums.NewType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class NewCreateRequest {

    @NotBlank(message = "Title is required")
    @Schema(description = "News title", example = "Big Sale Coming!")
    private String title;

    @NotBlank(message = "Content is required")
    @Schema(description = "News content", example = "Get ready for our biggest sale ever...")
    private String content;

    @NotBlank(message = "Category ID is required")
    @Schema(description = "Category ID of the news", example = "a1b2c3d4-e5f6-7890-gh12-ijklmnop3456")
    private String categoryId;

    @NotEmpty(message = "Please upload at least one image")
    @Size(max = 5, message = "You can upload up to 5 images")
    @Schema(description = "Upload up to 5 images", type = "array", implementation = MultipartFile.class)
    private List<ImageOfNewCreateRequest> images;

    @Schema(description = "Type of news", example = "PROMOTION")
    private NewType type;
}
