package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.ProductMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.ProductCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ProductResponse;
import edu.ut.sales.sales_analyst.model.entities.Category;
import edu.ut.sales.sales_analyst.model.entities.Product;
import edu.ut.sales.sales_analyst.repositories.CategoryRepo;
import edu.ut.sales.sales_analyst.repositories.ProductRepo;
import edu.ut.sales.sales_analyst.services.impl.IProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService {

    private final ProductRepo productRepo;

    private final CategoryRepo categoryRepo;

    private final ProductMapper productMapper;

    public ProductService(ProductRepo productRepo, CategoryRepo categoryRepo, ProductMapper productMapper) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponse createProduct(ProductCreateRequest productCreateRequest) {
        if(productRepo.findByProductName(productCreateRequest.getProductName()) != null) {
            throw new AppException(ErrorCode.PRODUCT_ALREADY_EXISTS);
        }
        Category existCategory = categoryRepo.findByCategoryId(productCreateRequest.getCategoryId());
        if(existCategory == null) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        Product product = new Product();
        product.setProductName(productCreateRequest.getProductName());
        product.setDescription(productCreateRequest.getDescription());
        product.setPrice(productCreateRequest.getPrice());
        product.setStockQuantity(productCreateRequest.getStockQuantity());
        product.setCategory(existCategory);
        productRepo.save(product);

        return productMapper.ToProductDTO(product);
    }

    @Override
    public ProductResponse getProduct(String productId) {
        Product product = productRepo.findByProductId(productId);
        if(product == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return productMapper.ToProductDTO(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepo.findAll(pageable);
        if (products.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_LIST_EMPTY);
        }
        return products.map(productMapper::ToProductDTO);
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
        product.setProductName(productCreateRequest.getProductName());
        product.setDescription(productCreateRequest.getDescription());
        product.setPrice(productCreateRequest.getPrice());
        product.setStockQuantity(productCreateRequest.getStockQuantity());
        product.setCategory(category);
        productRepo.save(product);

        return productMapper.ToProductDTO(product);
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
}
