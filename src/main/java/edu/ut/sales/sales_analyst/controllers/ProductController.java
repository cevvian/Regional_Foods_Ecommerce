package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.model.dtos.requests.ProductCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.RevenueFilterDTO;
import edu.ut.sales.sales_analyst.model.dtos.responses.ProductResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.model.dtos.responses.RevenueStatsDTO;
import edu.ut.sales.sales_analyst.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping()
    public ResponseAPI<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest productCreateRequest) {
        try {
            ProductResponse productResponse = productService.createProduct(productCreateRequest);
            return new ResponseAPI<>("Create product successfully", HttpStatus.CREATED, productResponse);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping()
    public ResponseAPI<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductResponse> productResponseList = productService.getAllProducts(pageable);
            return new ResponseAPI<>("Get all products successfully", HttpStatus.OK, productResponseList);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/{id}")
    public ResponseAPI<ProductResponse> getProduct(@PathVariable String id) {
        try {
            ProductResponse productResponse = productService.getProduct(id);
            return new ResponseAPI<>("Get product successfully", HttpStatus.OK, productResponse);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PutMapping("/{id}")
    public ResponseAPI<ProductResponse> updateProduct(@PathVariable String id, @RequestBody ProductCreateRequest productCreateRequest) {
        try {
            ProductResponse productResponse = productService.updateProduct(id, productCreateRequest);
            return new ResponseAPI<>("Update product successfully", HttpStatus.OK, productResponse);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseAPI<ProductResponse> deleteProduct(@PathVariable String id) {
        try {
            Boolean response = productService.deleteProduct(id);
            if (response) {
                return new ResponseAPI<>("Delete product successfully", HttpStatus.OK, null);
            }
            return new ResponseAPI<>("Delete product failed", HttpStatus.INTERNAL_SERVER_ERROR, null);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/statistics")
    public ResponseAPI<List<RevenueStatsDTO>> getRevenueStats(@RequestBody @Valid RevenueFilterDTO filter) {
        try {
            List<RevenueStatsDTO> stats = productService.getRevenueByTime(filter);
            return new ResponseAPI<>("Get revenue statistics successfully", HttpStatus.OK, stats);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (Exception e) {
            return new ResponseAPI<>("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

}
