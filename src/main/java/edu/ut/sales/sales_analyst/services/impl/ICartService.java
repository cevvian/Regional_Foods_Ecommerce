package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.requests.AddToCartRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.CartItemRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.CartItemResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.CartResponse;
import edu.ut.sales.sales_analyst.model.entities.CartItem;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

public interface ICartService {
    CartResponse addToCard(AddToCartRequest request);
    CartResponse viewCart();
    String deleteCartItem(String cartItemId);
    CartItemResponse updateCartItemQuantity(String cartItemId, CartItemRequest request);
    String deleteAllItemsByUser(String userId);
    Map<String, String> deleteCartItemList(List<String> cartItemIds);
    void deleteListCartItem(List<CartItem> cartItemList);
}
