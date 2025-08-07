package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.model.dtos.requests.ProductCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.ProductFilterRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.RevenueFilterDTO;
import edu.ut.sales.sales_analyst.model.dtos.responses.PageMeta;
import edu.ut.sales.sales_analyst.model.dtos.responses.ProductResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.model.dtos.responses.RevenueStatsDTO;
import edu.ut.sales.sales_analyst.model.entities.Product;
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

    @PostMapping
    public ResponseAPI<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest productCreateRequest) {
        ProductResponse productResponse = productService.createProduct(productCreateRequest);
        return new ResponseAPI<>("Create product successfully", HttpStatus.CREATED, productResponse);
    }

    @GetMapping
    public ResponseAPI<List<ProductResponse>> getAllProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> productPage = productService.getAllProducts(pageable);

        PageMeta meta = PageMeta.builder()
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .last(productPage.isLast())
                .build();

        return new ResponseAPI<>("Get all products successfully", HttpStatus.OK, productPage.getContent(), meta);
    }

    @PostMapping("/products/list")
    public ResponseAPI<List<Product>> createProductList(@RequestBody List<ProductCreateRequest> requests) {
        List<Product> productResponse = productService.createProductList(requests);
        return new ResponseAPI<>("Create product successfully", HttpStatus.CREATED, productResponse);
    }

    @GetMapping("/{id}")
    public ResponseAPI<ProductResponse> getProduct(@PathVariable String id) {
        ProductResponse productResponse = productService.getProduct(id);
        return new ResponseAPI<>("Get product successfully", HttpStatus.OK, productResponse);
    }

    @PutMapping("/{id}")
    public ResponseAPI<ProductResponse> updateProduct(
            @PathVariable String id,
            @RequestBody ProductCreateRequest productCreateRequest
    ) {
        ProductResponse productResponse = productService.updateProduct(id, productCreateRequest);
        return new ResponseAPI<>("Update product successfully", HttpStatus.OK, productResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseAPI<Void> deleteProduct(@PathVariable String id) {
        Boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return new ResponseAPI<>("Delete product successfully", HttpStatus.OK, null);
        } else {
            return new ResponseAPI<>("Delete product failed", HttpStatus.NOT_FOUND, null);
        }
    }

    @GetMapping("/statistics")
    public ResponseAPI<List<RevenueStatsDTO>> getRevenueStats(@ModelAttribute @Valid RevenueFilterDTO filter) {
        List<RevenueStatsDTO> stats = productService.getRevenueByTime(filter);
        return new ResponseAPI<>("Get revenue statistics successfully", HttpStatus.OK, stats);
    }

    @GetMapping("/filter")
    public ResponseAPI<List<ProductResponse>> filterProducts(
            @ModelAttribute @Valid ProductFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> productPage = productService.filterProducts(filter, pageable);

        PageMeta meta = PageMeta.builder()
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .last(productPage.isLast())
                .build();

        return new ResponseAPI<>("Filter product process successfully", HttpStatus.OK, productPage.getContent(), meta);
    }

}
