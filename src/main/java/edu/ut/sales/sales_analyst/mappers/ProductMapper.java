package edu.ut.sales.sales_analyst.mappers;

import edu.ut.sales.sales_analyst.model.dtos.responses.ProductResponse;
import edu.ut.sales.sales_analyst.model.entities.Product;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse ToProductDTO(Product product);
}
