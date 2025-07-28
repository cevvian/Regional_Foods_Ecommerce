package edu.ut.sales.sales_analyst.model.dtos.responses;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NewResponse {
    private String newId;
    private String title;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    private CategoryResponse category;

    private List<ImageOfNewResponse> images;
}
