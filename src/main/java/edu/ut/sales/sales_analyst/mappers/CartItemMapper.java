package edu.ut.sales.sales_analyst.mappers;

import edu.ut.sales.sales_analyst.model.dtos.responses.CartItemResponse;
import edu.ut.sales.sales_analyst.model.entities.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItemResponse toCartItemResponse(CartItem cartItem);
    CartItem fromCartItemResponse(CartItemResponse cartItemResponse);
}
