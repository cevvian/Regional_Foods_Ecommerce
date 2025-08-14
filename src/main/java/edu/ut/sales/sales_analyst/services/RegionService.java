package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.RegionMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.RegionRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.RegionResponse;
import edu.ut.sales.sales_analyst.model.entities.Region;
import edu.ut.sales.sales_analyst.repositories.RegionRepo;
import edu.ut.sales.sales_analyst.services.impl.IRegionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegionService implements IRegionService {
    RegionRepo regionRepo;
    RegionMapper regionMapper;

    @Override
    public RegionResponse createRegion(RegionRequest regionRequest) {
        Region region = regionRepo.findByRegionName(regionRequest.getRegionName());
        if (region != null) {
            throw new AppException(ErrorCode.REGION_ALREADY_EXISTS);
        }
        region = regionRepo.save(regionMapper.toRegion(regionRequest));
        return regionMapper.toResponse(region);
    }

    @Override
    public RegionResponse getRegionById(String id){
        Region region = regionRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REGION_NOT_FOUND));
        return regionMapper.toResponse(region);
    }

    @Override
    public Page<RegionResponse> getAllRegions(Pageable pageable){
        Page<Region> regions = regionRepo.findAll(pageable);
        return regions.map(regionMapper::toResponse);
    }

    @Override
    public RegionResponse updateRegion(String id, RegionRequest regionRequest) {
        Region region = regionRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.REGION_NOT_FOUND));
        region.setRegionName(regionRequest.getRegionName());
        return regionMapper.toResponse(regionRepo.save(region));
    }

    @Override
    public String deleteRegion(String id) {
        Region region = regionRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.REGION_NOT_FOUND));
        regionRepo.delete(region);
        boolean isDeleted = !regionRepo.existsById(id);
        return isDeleted
                ? "Successfully deleted Region"
                : "Failed to delete Region";
    }

    @Override
    public List<Region> addRegions(List<RegionRequest> regionRequests) {
        List<Region> regions = regionRequests.stream()
                .map(req -> Region.builder()
                        .regionName(req.getRegionName())
                        .build())
                .collect(Collectors.toList());

        return regionRepo.saveAll(regions);
    }

}
