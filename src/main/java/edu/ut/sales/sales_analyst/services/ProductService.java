package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.ProductMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.*;
import edu.ut.sales.sales_analyst.model.dtos.responses.ProductResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.RevenueStatsDTO;
import edu.ut.sales.sales_analyst.model.entities.*;
import edu.ut.sales.sales_analyst.repositories.CategoryRepo;
import edu.ut.sales.sales_analyst.repositories.ImageProductRepo;
import edu.ut.sales.sales_analyst.repositories.ProductRepo;
import edu.ut.sales.sales_analyst.repositories.RegionRepo;
import edu.ut.sales.sales_analyst.services.cloundinary.ImageUploadService;
import edu.ut.sales.sales_analyst.services.impl.IProductService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService implements IProductService {

    ProductRepo productRepo;
    CategoryRepo categoryRepo;
    ProductMapper productMapper;
    RegionRepo regionRepo;
    ImageUploadService imageUploadService;
    ImageProductRepo imageProductRepo;

    @Override
    public ProductResponse createProduct(ProductCreateRequest productCreateRequest) {
        if(productRepo.findByProductName(productCreateRequest.getProductName()) != null) {
            throw new AppException(ErrorCode.PRODUCT_ALREADY_EXISTS);
        }
        Category category = categoryRepo.findByCategoryId(productCreateRequest.getCategoryId());
        if(category == null) throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);

        Region region = regionRepo.findByRegionId(productCreateRequest.getRegionId());
        if(region == null) throw new AppException(ErrorCode.REGION_NOT_FOUND);

        Product product = productMapper.toProduct(productCreateRequest);
        product.setCategory(category);
        product.setRegion(region);
        product = productRepo.save(product);

        handleImageUploadAndAttachToProduct(product, productCreateRequest.getImages());

        return productMapper.toProductDTO(product);
    }

    @Override
    public ProductResponse getProduct(String productId) {
        Product product = productRepo.findByProductId(productId);
        if(product == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return productMapper.toProductDTO(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepo.findAll(pageable);
        return products.map(productMapper::toProductDTO);
    }

    @Override
    public List<Product> createProductList(List<ProductCreateRequest> requests) {
        List<Product> savedProducts = new ArrayList<>();

        for (ProductCreateRequest request : requests) {
            Category category = categoryRepo.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

            Region region = regionRepo.findById(request.getRegionId())
                    .orElseThrow(() -> new AppException(ErrorCode.REGION_NOT_FOUND));

            Product product = new Product();
            product.setProductName(request.getProductName());
            product.setDescription(request.getDescription());
            product.setPrice(request.getPrice());
            product.setStockQuantity(request.getStockQuantity());
            product.setCategory(category);
            product.setRegion(region);

            savedProducts.add(product);
        }

        productRepo.saveAll(savedProducts);

        return savedProducts;
    }

    @Override
    public ProductResponse updateProduct(String productId, ProductCreateRequest productCreateRequest) {
        Product product = productRepo.findByProductId(productId);
        if(product == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        Category category = categoryRepo.findByCategoryId(productCreateRequest.getCategoryId());
        if(category == null) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        if (productCreateRequest.getImages() != null && !productCreateRequest.getImages().isEmpty()) {
            imageProductRepo.deleteByProduct(product);
            handleImageUploadAndAttachToProduct(product, productCreateRequest.getImages());
        }

        Region region = regionRepo.findById(productCreateRequest.getRegionId())
                        .orElseThrow(() -> new AppException(ErrorCode.REGION_NOT_FOUND));
        product.setProductName(productCreateRequest.getProductName());
        product.setDescription(productCreateRequest.getDescription());
        product.setPrice(productCreateRequest.getPrice());
        product.setStockQuantity(productCreateRequest.getStockQuantity());
        product.setCategory(category);
        product.setRegion(region);
        productRepo.save(product);

        return productMapper.toProductDTO(product);
    }

    @Override
    public Boolean deleteProduct(String productId) {
        Product product = productRepo.findByProductId(productId);
        if(product == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        productRepo.delete(product);
        return true;
    }

    @Transactional
    public boolean softDeleteProduct(String productId) {
        Optional<Product> optionalProduct = productRepo.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setDeleted(true);
            productRepo.save(product);
            return true;
        }
        return false; // sản phẩm không tồn tại
    }

    @Override
    public List<RevenueStatsDTO> getRevenueByTime(RevenueFilterDTO filter) {
        if (filter.getMonth() != null && filter.getYear() == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        List<Object[]> raw = productRepo.getRevenueByTime(
                filter.getYear(),
                filter.getMonth(),
                filter.getProductId(),
                filter.getRegionId()
        );

        return raw.stream()
                .map(row -> new RevenueStatsDTO(
                        row[0].toString(),
                        row[1] != null ? ((Number) row[1]).doubleValue() : 0.0
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductResponse> filterProducts(ProductFilterRequest filterRequest, Pageable pageable) {
        Page<Product> products = productRepo.filterProducts(filterRequest.getCategoryId(), filterRequest.getRegionId(),
                filterRequest.getMinPrice(), filterRequest.getMaxPrice(), filterRequest.getMinStock(),
                filterRequest.getMinRating(), pageable);
        return products.map(productMapper::toProductDTO);
    }

    @Override
    public void increaseStock(String productId, int quantity) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepo.save(product);
    }

    private void handleImageUploadAndAttachToProduct(Product product, List<ImageProductCreationRequest> imageRequests) {
        if (imageRequests == null || imageRequests.isEmpty()) {
            throw new AppException(ErrorCode.FILE_UPLOAD_NOT_FOUND);
        }

        List<MultipartFile> files = imageRequests.stream()
                .map(ImageProductCreationRequest::getImage)
                .collect(Collectors.toList());

        List<String> imageUrls;
        try {
            imageUrls = imageUploadService.uploadNewsImages(files, product.getProductId());
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        List<ImageProduct> imageEntities = new ArrayList<>();
        for (int i = 0; i < imageRequests.size(); i++) {
            String imageUrl = imageUrls.get(i);

            ImageProduct imageEntity = ImageProduct.builder()
                    .product(product)
                    .imageUrl(imageUrl)
                    .build();

            imageEntities.add(imageEntity);
        }
        imageProductRepo.saveAll(imageEntities);
        product.setImages(imageEntities);
    }
}
