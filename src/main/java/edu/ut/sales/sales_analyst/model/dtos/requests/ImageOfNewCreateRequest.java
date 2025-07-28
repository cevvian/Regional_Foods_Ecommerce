package edu.ut.sales.sales_analyst.model.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageOfNewCreateRequest {

    @Schema(description = "Type of content, e.g. NEWS, PRODUCT,...", example = "NEWS", required = true)
    private String typeContent;

    @Schema(type = "string", format = "binary", description = "Image file to upload", required = true)
    private MultipartFile file;
}