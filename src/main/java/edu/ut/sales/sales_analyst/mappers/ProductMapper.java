package edu.ut.sales.sales_analyst.mappers;

import edu.ut.sales.sales_analyst.model.dtos.requests.ProductCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ProductResponse;
import edu.ut.sales.sales_analyst.model.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ImageProductMapper.class})
public interface ProductMapper {
    @Mapping(target = "imageProductResponseList", source = "images")
    @Mapping(target = "isDeleted", source = "deleted")
    ProductResponse toProductDTO(Product product);
    Product toProduct(ProductCreateRequest request);
    Product toProductEntity(ProductResponse productResponse);
}
