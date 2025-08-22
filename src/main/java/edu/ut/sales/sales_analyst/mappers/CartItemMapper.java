package edu.ut.sales.sales_analyst.mappers;

import edu.ut.sales.sales_analyst.model.dtos.requests.CartItemRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.CartItemResponse;
import edu.ut.sales.sales_analyst.model.entities.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CartItemMapper {
    CartItemResponse toCartItemResponse(CartItem cartItem);
    CartItem fromCartItemResponse(CartItemResponse cartItemResponse);
    List<CartItem> toCartItem(List<CartItemRequest> cartItemRequest);
}
