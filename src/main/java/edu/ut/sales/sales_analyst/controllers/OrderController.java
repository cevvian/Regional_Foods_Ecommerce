package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.model.dtos.requests.OrderCartCreationRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.OrderCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.OrderResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.PageMeta;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.model.enums.OrderStatus;
import edu.ut.sales.sales_analyst.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/order")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Order", description = "APIs for order product and management order")
public class OrderController {
    OrderService orderService;

    @Operation(summary = "Create a new order", description = "Add a new order")
    @PostMapping
    public ResponseAPI<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return new ResponseAPI<>("Create order successfully", HttpStatus.CREATED, response);
    }

    @Operation(summary = "Create a new order from cart", description = "Add a new order from cart")
    @PostMapping("/cart")
    public ResponseAPI<OrderResponse> createOrderFromCart(@Valid @RequestBody OrderCartCreationRequest request) {
        OrderResponse response = orderService.createOrderFromCart(request);
        return new ResponseAPI<>("Create order successfully", HttpStatus.CREATED, response);
    }

    @Operation(summary = "Get order by ID", description = "Retrieve order details by order ID")
    @GetMapping("/{orderId}")
    public ResponseAPI<OrderResponse> getOrder(@PathVariable String orderId) {
        OrderResponse response = orderService.getOrder(orderId);
        return new ResponseAPI<>("Get order successfully", HttpStatus.OK, response);
    }

    @Operation(summary = "Get all active orders", description = "Retrieve all active orders with pagination")
    @GetMapping("/active")
    public ResponseAPI<List<OrderResponse>> getActiveOrder(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponse> orderPage = orderService.getOrdersActive(pageable);

        PageMeta meta = PageMeta.builder()
                .page(orderPage.getNumber())
                .size(orderPage.getSize())
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .last(orderPage.isLast())
                .build();

        return new ResponseAPI<>("Get orders successfully", HttpStatus.OK, orderPage.getContent(), meta);
    }

    @Operation(summary = "Get all active orders", description = "Retrieve all active orders with pagination")
    @GetMapping("/filter")
    public ResponseAPI<List<OrderResponse>> getOrdersByStatus(
            @RequestParam(name = "status", required = false) OrderStatus status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponse> orderPage = orderService.getOrdersByStatusAndActive(pageable, status);

        PageMeta meta = PageMeta.builder()
                .page(orderPage.getNumber())
                .size(orderPage.getSize())
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .last(orderPage.isLast())
                .build();

        return new ResponseAPI<>("Get orders successfully", HttpStatus.OK, orderPage.getContent(), meta);
    }

    @Operation(summary = "Get all active orders by status", description = "Retrieve all active orders with pagination")
    @GetMapping("/user/{userId}")
    public ResponseAPI<List<OrderResponse>> getOrdersByStatus(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponse> orderPage = orderService.getOrdersByCustomerId(userId, pageable);

        PageMeta meta = PageMeta.builder()
                .page(orderPage.getNumber())
                .size(orderPage.getSize())
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .last(orderPage.isLast())
                .build();

        return new ResponseAPI<>("Get orders successfully", HttpStatus.OK, orderPage.getContent(), meta);
    }

    @Operation(summary = "Update order", description = "Update an existing order by order ID")
    @PutMapping("/{orderId}")
    public ResponseAPI<OrderResponse> updateOrder(
            @PathVariable String orderId,
            @Valid @RequestBody OrderCreateRequest request
    ) {
        OrderResponse response = orderService.updateOrder(orderId, request);
        return new ResponseAPI<>("Update order successfully", HttpStatus.OK, response);
    }

    @Operation(summary = "Update order status", description = "Update an existing order by order ID")
    @PutMapping("/{orderId}/status")
    public ResponseAPI<OrderResponse> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam OrderStatus status
    ) {
        OrderResponse response = orderService.updateOrderStatus(orderId, status);
        return new ResponseAPI<>("Update order successfully", HttpStatus.OK, response);
    }

    @Operation(summary = "Delete order", description = "Delete a order by ID")
    @DeleteMapping("/{orderId}")
    public ResponseAPI<Boolean> deleteOrder(@PathVariable String orderId) {
        Boolean deleted = orderService.deleteOrder(orderId);
        return new ResponseAPI<>("Delete order successfully", HttpStatus.OK, deleted);
    }

    @Operation(summary = "Cancel order", description = "Cancel a order by ID")
    @PostMapping("/{orderId}/cancel")
    public ResponseAPI<OrderResponse> cancelOrder(@PathVariable String orderId) {
        OrderResponse response = orderService.cancelOrder(orderId);
        return new ResponseAPI<>("Cancel order successfully", HttpStatus.OK, response);
    }
}
