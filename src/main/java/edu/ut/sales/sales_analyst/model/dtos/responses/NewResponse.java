package edu.ut.sales.sales_analyst.model.dtos.responses;

import edu.ut.sales.sales_analyst.model.enums.NewType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class NewResponse {
    private String newId;
    private String title;
    private String content;
    private Date createAt;
    private Date updateAt;
    private NewType type;

    private CategoryResponse category;

    private List<ImageOfNewResponse> images;
}
