package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.CartItemMapper;
import edu.ut.sales.sales_analyst.mappers.CartMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.AddToCartRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.CartItemRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.CartItemResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.CartResponse;
import edu.ut.sales.sales_analyst.model.entities.*;
import edu.ut.sales.sales_analyst.repositories.CartItemRepo;
import edu.ut.sales.sales_analyst.repositories.CartRepo;
import edu.ut.sales.sales_analyst.repositories.ProductRepo;
import edu.ut.sales.sales_analyst.repositories.UserRepo;
import edu.ut.sales.sales_analyst.services.impl.ICartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService implements ICartService {
    CartRepo cartRepo;
    CartItemRepo cartItemRepo;
    CartMapper cartMapper;
    CartItemMapper cartItemMapper;
    UserRepo userRepo;
    ProductRepo productRepo;
    UserService userService;

    @Override
    public CartResponse addToCard(AddToCartRequest request) {
        User userCurrent = userService.getCurrentUser();
        Cart cart = cartRepo.findByUser(userCurrent)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(userCurrent);
                    return cartRepo.save(newCart);
                });

        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Optional<CartItem> existingItemOpt = cartItemRepo.findByCartAndProduct(cart, product);
        if (existingItemOpt.isPresent()) {
            CartItem item = existingItemOpt.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepo.save(item);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
            cartItemRepo.save(item);
        }
        cartRepo.save(cart);
        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse viewCart() {
        User userCurrent = userService.getCurrentUser();
        User user = userRepo.findByUserId(userCurrent.getUserId());
        if(user == null) throw new AppException(ErrorCode.USER_NOT_FOUND);

        Cart cart = cartRepo.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepo.save(newCart);
                });

        return cartMapper.toCartResponse(cart);
    }

    @Override
    @Transactional
    public String deleteCartItem(String cartItemId) {
        User currentUser = userService.getCurrentUser();

        CartItem cartItem = cartItemRepo.findByIdAndCartUserId(cartItemId, currentUser.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        cartItemRepo.delete(cartItem);
        boolean isDeleted = !cartItemRepo.existsById(cartItemId);
        return isDeleted
                ? "Successfully deleted Item"
                : "Failed to delete Item";
    }

    @Override
    public void deleteListCartItem(List<CartItem> cartItemList) {
        if (cartItemList.isEmpty()) {
            throw new AppException(ErrorCode.CART_EMPTY);
        }

        // Lấy danh sách ID
        List<String> ids = cartItemList.stream()
                .map(CartItem::getCartItemId)
                .toList();

        cartItemRepo.deleteAllInBatch(cartItemList); // Nhanh hơn deleteAll
    }

    @Override
    public CartItemResponse updateCartItemQuantity(String cartItemId, CartItemRequest request){
        User currentUser = userService.getCurrentUser();

        CartItem cartItem = cartItemRepo.findByIdAndCartUserId(cartItemId, currentUser.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
        cartItem.setQuantity(request.getQuantity());
        cartItemRepo.save(cartItem);
        return cartItemMapper.toCartItemResponse(cartItem);
    }

    @Override
    public Map<String, String> deleteCartItemList(List<String> cartItemIds) {
        Map<String, String> result = new HashMap<>();

        for (String cartItemId : cartItemIds) {
            try {
                User currentUser = userService.getCurrentUser();

                CartItem cartItem = cartItemRepo.findByIdAndCartUserId(cartItemId, currentUser.getUserId())
                        .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
                cartItemRepo.delete(cartItem);
                boolean isDeleted = !cartItemRepo.existsById(cartItemId);
                result.put(cartItemId, isDeleted ? "Deleted" : "Failed to delete");
            } catch (AppException e) {
                result.put(cartItemId, "Not found");
            } catch (Exception e) {
                result.put(cartItemId, "Error: " + e.getMessage());
            }
        }
        return result;
    }

    @Override
    public String deleteAllItemsByUser() {
        User userCurrent = userService.getCurrentUser();
        Cart cart = cartRepo.findByUser_UserId(userCurrent.getUserId());
        if (cart == null) throw new AppException(ErrorCode.CART_NOT_FOUND);

        List<CartItem> items = cartItemRepo.findAllByUserId(userCurrent.getUserId());
        if (items.isEmpty()) {
            return "No items found for user: " + userCurrent.getUserId();
        }
        cartItemRepo.deleteAll(items);

        boolean isDeletedAll = cartItemRepo.findAllByUserId(userCurrent.getUserId()).isEmpty();
        return isDeletedAll
                ? "Successfully deleted all items for user: " + userCurrent.getUserId()
                : "Failed to delete some items for user: " + userCurrent.getUserId();
    }
}
