package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.model.dtos.requests.RegionRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.PageMeta;
import edu.ut.sales.sales_analyst.model.dtos.responses.RegionResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.model.entities.Region;
import edu.ut.sales.sales_analyst.services.RegionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/region")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegionController {
    RegionService regionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseAPI<RegionResponse> create(@Valid @RequestBody RegionRequest request) {
        RegionResponse response = regionService.createRegion(request);
        return new ResponseAPI<>("Create region successfully", HttpStatus.CREATED, response);
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseAPI<List<Region>> addRegions(@RequestBody @Valid List<RegionRequest> regionRequests) {
        List<Region> createdRegions = regionService.addRegions(regionRequests);
        return new ResponseAPI<>("Create list region successfully", HttpStatus.CREATED, createdRegions);
    }

    @GetMapping
    public ResponseAPI<List<RegionResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RegionResponse> regionPage = regionService.getAllRegions(pageable);

        PageMeta meta = PageMeta.builder()
                .page(regionPage.getNumber())
                .size(regionPage.getSize())
                .totalElements(regionPage.getTotalElements())
                .totalPages(regionPage.getTotalPages())
                .last(regionPage.isLast())
                .build();

        return new ResponseAPI<>("Get all regions", HttpStatus.OK, regionPage.getContent(), meta);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseAPI<RegionResponse> getById(@PathVariable String id) {
        RegionResponse response = regionService.getRegionById(id);
        return new ResponseAPI<>("Get region by id successfully", HttpStatus.OK, response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseAPI<RegionResponse> update(@PathVariable String id, @Valid @RequestBody RegionRequest request) {
        RegionResponse response = regionService.updateRegion(id, request);
        return new ResponseAPI<>("Update successfully", HttpStatus.OK, response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseAPI<String> delete(@PathVariable String id) {
        String response = regionService.deleteRegion(id);
        return new ResponseAPI<>("Delete successfully", HttpStatus.OK, response);
    }
}
