package edu.ut.sales.sales_analyst.mappers;

import edu.ut.sales.sales_analyst.model.dtos.responses.OrderResponse;
import edu.ut.sales.sales_analyst.model.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "user", target = "userResponse")
    @Mapping(source = "address", target = "addressResponse")
    @Mapping(source = "orderItems", target = "orderItemResponses")
    OrderResponse toOrderResponse(Order order);
}
