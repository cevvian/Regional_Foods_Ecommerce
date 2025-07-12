package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.requests.ProductCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    ProductResponse createProduct(ProductCreateRequest productCreateRequest);
    ProductResponse getProduct(String productId);
    Page<ProductResponse> getAllProducts(Pageable pageable);
    ProductResponse updateProduct(String productId, ProductCreateRequest productCreateRequest);
    Boolean deleteProduct(String productId);
}
