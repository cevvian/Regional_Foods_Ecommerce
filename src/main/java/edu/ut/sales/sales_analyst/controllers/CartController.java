package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.exceptions.AppException;
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
import org.springframework.web.bind.annotation.*;

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
    public ResponseAPI<CartResponse> viewMyCart(@PathVariable String userId) {
        try {
            CartResponse cartResponse = cartService.viewCart(userId);
            return new ResponseAPI<>("View cart successfully", HttpStatus.OK, cartResponse);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping()
    public ResponseAPI<CartResponse> addNewItem(@Valid @RequestBody AddToCartRequest cartRequest){
        try {
            CartResponse cartResponse = cartService.addToCard(cartRequest);
            return new ResponseAPI<>("Add new cart item successfully", HttpStatus.CREATED, cartResponse);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseAPI<String> deleteItem(@PathVariable String id){
        try {
            String response = cartService.deleteCartItem(id);
            return new ResponseAPI<>("Delete successfully", HttpStatus.OK, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/cart-item")
    public ResponseAPI<Map<String, String>> deleteItems(List<String> id){
        try {
            Map<String, String> response = cartService.deleteCartItemList(id);
            return new ResponseAPI<>("Delete successfully", HttpStatus.OK, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/cart-item/{userId}")
    public ResponseAPI<String> deleteAllItemsByUser(@PathVariable String userId){
        try {
            String response = cartService.deleteAllItemsByUser(userId);
            return new ResponseAPI<>("Delete successfully", HttpStatus.OK, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PutMapping("/{id}")
    public ResponseAPI<CartItemResponse> updateCartItemQuantity(@PathVariable String id,
                                                                @Valid @RequestBody CartItemRequest request){
        try {
            CartItemResponse response = cartService.updateCartItemQuantity(id, request);
            return new ResponseAPI<>("Update item quantity successfully", HttpStatus.OK, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
