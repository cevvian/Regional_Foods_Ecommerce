package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.model.dtos.requests.RegionRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.RegionResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
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
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/region")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegionController {
    RegionService regionService;

    @PostMapping()
    public ResponseAPI<RegionResponse> create(@Valid @RequestBody RegionRequest request){
        try {
            RegionResponse response = regionService.createRegion(request);
            return new ResponseAPI<>("Create region successfully", HttpStatus.CREATED, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping()
    public ResponseAPI<Page<RegionResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<RegionResponse> responses = regionService.getAllRegions(pageable);
            return new ResponseAPI<>("Get all regions", HttpStatus.OK, responses);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/{id}")
    public ResponseAPI<RegionResponse> getById(@PathVariable String id) {
        try {
            RegionResponse response = regionService.getRegionById(id);
            return new ResponseAPI<>("Get region by id successfully", HttpStatus.CREATED, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PutMapping("/{id}")
    public ResponseAPI<RegionResponse> update(@PathVariable String id, @Valid @RequestBody RegionRequest request){
        try {
            RegionResponse response = regionService.updateRegion(id, request);
            return new ResponseAPI<>("Update successfully", HttpStatus.CREATED, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseAPI<String> delete(@PathVariable String id) {
        try {
            String response = regionService.deleteRegion(id);
            return new ResponseAPI<>("Delete successfully", HttpStatus.CREATED, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
