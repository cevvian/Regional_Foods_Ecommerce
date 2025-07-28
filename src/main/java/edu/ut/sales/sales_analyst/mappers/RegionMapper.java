package edu.ut.sales.sales_analyst.mappers;

import edu.ut.sales.sales_analyst.model.dtos.requests.RegionRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.RegionResponse;
import edu.ut.sales.sales_analyst.model.entities.Region;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegionMapper {
    RegionResponse toResponse(Region region);
        Region toRegion(RegionRequest regionRequest);
}
