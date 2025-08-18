package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.model.dtos.requests.AddToCartRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.CartItemRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.CartItemResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.CartResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.services.CartService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/my-cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseAPI<CartResponse> viewMyCart(@PathVariable String userId) {
        CartResponse cartResponse = cartService.viewCart(userId);
        return new ResponseAPI<>("View cart successfully", HttpStatus.OK, cartResponse);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseAPI<CartResponse> addNewItem(@Valid @RequestBody AddToCartRequest cartRequest) {
        CartResponse cartResponse = cartService.addToCard(cartRequest);
        return new ResponseAPI<>("Add new cart item successfully", HttpStatus.CREATED, cartResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseAPI<String> deleteItem(@PathVariable String id) {
        String response = cartService.deleteCartItem(id);
        return new ResponseAPI<>("Delete successfully", HttpStatus.OK, response);
    }

    @DeleteMapping("/cart-item")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseAPI<Map<String, String>> deleteItems(@RequestBody List<String> ids) {
        Map<String, String> response = cartService.deleteCartItemList(ids);
        return new ResponseAPI<>("Delete successfully", HttpStatus.OK, response);
    }

    @DeleteMapping("/cart-item/by-user")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseAPI<String> deleteAllItemsByUser() {
        String response = cartService.deleteAllItemsByUser();
        return new ResponseAPI<>("Delete successfully", HttpStatus.OK, response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseAPI<CartItemResponse> updateCartItemQuantity(@PathVariable String id,
                                                                @Valid @RequestBody CartItemRequest request) {
        CartItemResponse response = cartService.updateCartItemQuantity(id, request);
        return new ResponseAPI<>("Update item quantity successfully", HttpStatus.OK, response);
    }
}
