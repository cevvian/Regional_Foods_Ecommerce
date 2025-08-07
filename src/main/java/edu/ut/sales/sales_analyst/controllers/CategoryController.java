package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.model.dtos.requests.CategoryRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.CategoryResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.PageMeta;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.services.CategoryService;
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

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryController {
    CategoryService categoryService;

    @PostMapping()
    public ResponseAPI<CategoryResponse> create(@Valid @RequestBody CategoryRequest request){
        try {
            CategoryResponse response = categoryService.createCategory(request);
            return new ResponseAPI<>("Create category successfully", HttpStatus.CREATED, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping
    public ResponseAPI<List<CategoryResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<CategoryResponse> categoryPage = categoryService.getAllCategories(pageable);

            PageMeta meta = PageMeta.builder()
                    .page(categoryPage.getNumber())
                    .size(categoryPage.getSize())
                    .totalElements(categoryPage.getTotalElements())
                    .totalPages(categoryPage.getTotalPages())
                    .last(categoryPage.isLast())
                    .build();

            return new ResponseAPI<>("Get all categories", HttpStatus.OK, categoryPage.getContent(), meta);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


    @GetMapping("/{id}")
    public ResponseAPI<CategoryResponse> getById(@PathVariable String id) {
        try {
            CategoryResponse response = categoryService.getCategoryById(id);
            return new ResponseAPI<>("Get category by id successfully", HttpStatus.CREATED, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PutMapping("/{id}")
    public ResponseAPI<CategoryResponse> update(@PathVariable String id, @Valid @RequestBody CategoryRequest request){
        try {
            CategoryResponse response = categoryService.updateCategory(id, request);
            return new ResponseAPI<>("Update successfully", HttpStatus.CREATED, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseAPI<String> delete(@PathVariable String id) {
        try {
            String response = categoryService.deleteCategory(id);
            return new ResponseAPI<>("Delete successfully", HttpStatus.CREATED, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
