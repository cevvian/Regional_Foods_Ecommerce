package edu.ut.sales.sales_analyst.mappers;

import edu.ut.sales.sales_analyst.model.dtos.responses.OrderItemResponse;
import edu.ut.sales.sales_analyst.model.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderItemMapper {
    @Mapping(source = "product", target = "productResponse")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
}

