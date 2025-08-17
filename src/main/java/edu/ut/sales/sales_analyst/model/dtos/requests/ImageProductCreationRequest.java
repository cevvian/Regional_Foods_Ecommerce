package edu.ut.sales.sales_analyst.model.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageProductCreationRequest {
    @NotNull(message = "Image file is required")
    private MultipartFile image;
    @NotBlank
    private String productId;
}
