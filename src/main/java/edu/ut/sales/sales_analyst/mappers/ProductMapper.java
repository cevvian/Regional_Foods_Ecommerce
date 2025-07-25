package edu.ut.sales.sales_analyst.mappers;

import edu.ut.sales.sales_analyst.model.dtos.requests.ProductCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ProductResponse;
import edu.ut.sales.sales_analyst.model.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toProductDTO(Product product);
    Product toProduct(ProductCreateRequest request);
}
