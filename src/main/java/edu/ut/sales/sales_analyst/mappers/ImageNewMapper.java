package edu.ut.sales.sales_analyst.mappers;

import edu.ut.sales.sales_analyst.model.dtos.responses.ImageNewResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.ImageOfNewResponse;
import edu.ut.sales.sales_analyst.model.entities.ImageNew;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageNewMapper {
    @Mapping(source = "news.newId", target = "newId")
    ImageNewResponse toImageNewResponse(ImageNew image);
    ImageOfNewResponse toImageOfNewResponse(ImageNew image);
    List<ImageOfNewResponse> toResponseList(List<ImageNew> imageNewList);
}
