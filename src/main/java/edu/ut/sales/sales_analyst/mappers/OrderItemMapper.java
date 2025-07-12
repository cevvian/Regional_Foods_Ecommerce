package edu.ut.sales.sales_analyst.mappers;

import edu.ut.sales.sales_analyst.model.dtos.responses.OrderItemResponse;
import edu.ut.sales.sales_analyst.model.entities.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
}
