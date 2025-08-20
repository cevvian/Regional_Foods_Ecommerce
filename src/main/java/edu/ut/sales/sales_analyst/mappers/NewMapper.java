package edu.ut.sales.sales_analyst.mappers;

import edu.ut.sales.sales_analyst.model.dtos.responses.NewResponse;
import edu.ut.sales.sales_analyst.model.entities.New;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",  uses = {ImageNewMapper.class})
public interface NewMapper {
    @Mapping(source = "type", target = "type")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "images", target = "images")
    NewResponse ToNewResponse(New news);
}
