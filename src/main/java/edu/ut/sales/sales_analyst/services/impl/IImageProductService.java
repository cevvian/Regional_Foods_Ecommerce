package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.requests.ImageProductCreationRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ImageProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IImageProductService {
    ImageProductResponse create(ImageProductCreationRequest request);
    void delete(String id);
    ImageProductResponse getById(String id);
    Page<ImageProductResponse> getAll(Pageable pageable);
    Page<ImageProductResponse> getByProductId(String productId, Pageable pageable);
    ImageProductResponse update(String id, ImageProductCreationRequest request);
}
