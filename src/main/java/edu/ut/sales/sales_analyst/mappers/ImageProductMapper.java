package edu.ut.sales.sales_analyst.mappers;

import edu.ut.sales.sales_analyst.model.dtos.responses.ImageProductResponse;
import edu.ut.sales.sales_analyst.model.entities.ImageProduct;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageProductMapper {
    ImageProductResponse toImageProductResponse(ImageProduct image);
    List<ImageProductResponse> toResponseList(List<ImageProduct> imageProductList);
}
