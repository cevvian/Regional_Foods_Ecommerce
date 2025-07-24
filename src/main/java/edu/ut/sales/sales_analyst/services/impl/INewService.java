package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.requests.NewCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.NewUpdateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.NewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface INewService {
    NewResponse createNews(NewCreateRequest request);

    NewResponse getNews(String newId);

    Page<NewResponse> getAllNews(Pageable pageable);

    NewResponse updateNews(String newId, NewUpdateRequest request);

    Boolean deleteNews(String newId);
}
