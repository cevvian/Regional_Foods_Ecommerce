package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.ProductMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.ProductCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.RevenueFilterDTO;
import edu.ut.sales.sales_analyst.model.dtos.responses.ProductResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.RevenueStatsDTO;
import edu.ut.sales.sales_analyst.model.entities.Category;
import edu.ut.sales.sales_analyst.model.entities.Product;
import edu.ut.sales.sales_analyst.model.entities.Region;
import edu.ut.sales.sales_analyst.repositories.CategoryRepo;
import edu.ut.sales.sales_analyst.repositories.ProductRepo;
import edu.ut.sales.sales_analyst.repositories.RegionRepo;
import edu.ut.sales.sales_analyst.services.impl.IProductService;
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

import java.util.List;
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
        return productMapper.toProductDTO(product);
    }

    public ProductResponse getProduct(String productId) {
        Product product = productRepo.findByProductId(productId);
        if(product == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return productMapper.toProductDTO(product);
    }

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepo.findAll(pageable);
        if (products.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_LIST_EMPTY);
        }
        return products.map(productMapper::toProductDTO);
    }

    public ProductResponse updateProduct(String productId, ProductCreateRequest productCreateRequest) {
        Product product = productRepo.findByProductId(productId);
        if(product == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        Category category = categoryRepo.findByCategoryId(productCreateRequest.getCategoryId());
        if(category == null) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
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

    public Boolean deleteProduct(String productId) {
        Product product = productRepo.findByProductId(productId);
        if(product == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        productRepo.delete(product);
        return true;
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
}
