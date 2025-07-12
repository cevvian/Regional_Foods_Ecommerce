package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.ProductMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.ProductCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ProductResponse;
import edu.ut.sales.sales_analyst.model.entities.Product;
import edu.ut.sales.sales_analyst.repositories.ProductRepo;
import edu.ut.sales.sales_analyst.services.impl.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(ProductCreateRequest productCreateRequest) {
        if(productRepo.findByProductName(productCreateRequest.getProductName()) != null) {
            throw new AppException(ErrorCode.PRODUCT_ALREADY_EXISTS);
        }
        Product product = new Product();
        product.setProductName(productCreateRequest.getProductName());
        product.setDescription(productCreateRequest.getDescription());
        product.setPrice(productCreateRequest.getPrice());
        product.setStockQuantity(productCreateRequest.getStockQuantity());
        product.setCategory(productCreateRequest.getCategory());
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

        product.setProductName(productCreateRequest.getProductName());
        product.setDescription(productCreateRequest.getDescription());
        product.setPrice(productCreateRequest.getPrice());
        product.setStockQuantity(productCreateRequest.getStockQuantity());
        product.setCategory(productCreateRequest.getCategory());
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
