package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.requests.ImageNewCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ImageNewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IImageNewService {
    ImageNewResponse create(ImageNewCreateRequest request);
    ImageNewResponse update(String id, ImageNewCreateRequest request);
    void delete(String id);
    ImageNewResponse getById(String id);
    Page<ImageNewResponse> getAll(Pageable pageable);
    Page<ImageNewResponse> getByNewsId(String newsId, Pageable pageable);
}
