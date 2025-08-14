package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.CategoryMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.CategoryRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.CategoryResponse;
import edu.ut.sales.sales_analyst.model.entities.Category;
import edu.ut.sales.sales_analyst.repositories.CategoryRepo;
import edu.ut.sales.sales_analyst.services.impl.ICategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryService implements ICategoryService {
    CategoryRepo categoryRepo;
    CategoryMapper categoryMapper;

    @Override
    public Page<CategoryResponse> getAllCategories(Pageable pageable){
        Page<Category> categories = categoryRepo.findAll(pageable);
        if (categories.isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        return categories.map(categoryMapper::toResponse);
    }

    @Override
    public CategoryResponse getCategoryById(String id){
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return categoryMapper.toResponse(category);
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest){
        Category category = categoryRepo.findByCategoryName(categoryRequest.getCategoryName());
        if (category != null) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }
        category = categoryRepo.save(categoryMapper.toCategory(categoryRequest));
        return categoryMapper.toResponse(category);
    }

    @Override
    public CategoryResponse updateCategory(String categoryId,CategoryRequest categoryRequest){
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        category.setCategoryName(categoryRequest.getCategoryName());
        category.setDescription(categoryRequest.getDescription());
        return categoryMapper.toResponse(categoryRepo.save(category));
    }

    @Override
    public String deleteCategory(String categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        categoryRepo.delete(category);
        boolean isDeleted = !categoryRepo.existsById(categoryId);

        return isDeleted
                ? "Successfully deleted Category"
                : "Failed to delete Category";
    }
}
