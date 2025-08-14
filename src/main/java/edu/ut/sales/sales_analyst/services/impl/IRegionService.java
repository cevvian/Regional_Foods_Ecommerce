package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.requests.RegionRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.RegionResponse;
import edu.ut.sales.sales_analyst.model.entities.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRegionService {
    RegionResponse createRegion(RegionRequest regionRequest);
    RegionResponse getRegionById(String id);
    Page<RegionResponse> getAllRegions(Pageable pageable);
    RegionResponse updateRegion(String id, RegionRequest regionRequest);
    String deleteRegion(String id);
    List<Region> addRegions(List<RegionRequest> regionRequests);
}
