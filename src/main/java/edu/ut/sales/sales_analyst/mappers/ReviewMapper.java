package edu.ut.sales.sales_analyst.mappers;

import edu.ut.sales.sales_analyst.model.dtos.responses.ReviewResponse;
import edu.ut.sales.sales_analyst.model.entities.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
public interface ReviewMapper {

    ReviewResponse toReviewResponse(Review review);
}
