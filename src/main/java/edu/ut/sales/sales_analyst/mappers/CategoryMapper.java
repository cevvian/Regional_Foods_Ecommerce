package edu.ut.sales.sales_analyst.mappers;

import edu.ut.sales.sales_analyst.model.dtos.requests.CategoryRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.CategoryResponse;
import edu.ut.sales.sales_analyst.model.entities.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);
    Category toCategory(CategoryRequest categoryRequest);
}
