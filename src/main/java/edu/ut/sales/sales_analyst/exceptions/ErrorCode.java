package edu.ut.sales.sales_analyst.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    // ---------------- USER (1000–1299) ----------------

    // Not Found (1000–1099)
    USER_NOT_FOUND(1000, "USER not found", HttpStatus.NOT_FOUND),
    USER_PHONE_NOT_FOUND(1001, "USER's phone not found", HttpStatus.NOT_FOUND),
    USER_EMAIL_NOT_FOUND(1002, "USER's email not found", HttpStatus.NOT_FOUND),
    LIST_USER_NOT_FOUND(1003, "List of USERs not found", HttpStatus.NOT_FOUND),

    // Already Exists (1100–1149)
    USER_ALREADY_EXISTS(1100, "USER already exists", HttpStatus.CONFLICT),

    // Validation Errors (1150–1199)
    USER_NAME_REQUIRED(1150, "USER name is required", HttpStatus.BAD_REQUEST),
    USER_EMAIL_INVALID(1151, "Invalid email format", HttpStatus.BAD_REQUEST),
    USER_PHONE_INVALID(1152, "Invalid phone number format", HttpStatus.BAD_REQUEST),
    USER_ADDRESS_REQUIRED(1153, "Address cannot be blank", HttpStatus.BAD_REQUEST),

    // ---------------- PRODUCT (1300–1499) ----------------

    // Not Found (1300–1349)
    PRODUCT_NOT_FOUND(1300, "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_LIST_EMPTY(1301, "No products available", HttpStatus.NOT_FOUND),

    // Already Exists (1350–1374)
    PRODUCT_ALREADY_EXISTS(1350, "Product already exists", HttpStatus.CONFLICT),

    // Validation Errors (1375–1399)
    PRODUCT_INVALID_PRICE(1375, "Price must be greater than 0", HttpStatus.BAD_REQUEST),
    PRODUCT_INVALID_STOCK(1376, "Stock quantity cannot be negative", HttpStatus.BAD_REQUEST),

    // ---------------- ORDER (1600–1799) ----------------

    // Not Found (1600–1649)
    ORDER_NOT_FOUND(1600, "Order not found", HttpStatus.NOT_FOUND),
    ORDER_LIST_EMPTY(1601, "No orders found", HttpStatus.NOT_FOUND),

    // Already Exists (1650–1674)
    ORDER_ALREADY_EXISTS(1650, "Order already exists", HttpStatus.CONFLICT),

    // Business Rule Errors (1675–1699)
    ORDER_ALREADY_CANCELLED(1675, "Order is already cancelled", HttpStatus.BAD_REQUEST),
    ORDER_CANNOT_UPDATE_CANCELLED(1676, "Cannot update a cancelled order", HttpStatus.BAD_REQUEST),
    ORDER_INVALID_STATUS_TRANSITION(1677, "Invalid status transition", HttpStatus.BAD_REQUEST),

    // ---------------- ORDER ITEM (1900–1999) ----------------

    // Not Found (1900–1949)
    ORDER_ITEM_NOT_FOUND(1900, "Order item not found", HttpStatus.NOT_FOUND),

    // Validation Errors (1950–1999)
    ORDER_ITEM_QUANTITY_INVALID(1950, "Order item must have quantity >= 1", HttpStatus.BAD_REQUEST),
    ORDER_ITEM_PRODUCT_ID_MISSING(1951, "Product ID in order item is missing", HttpStatus.BAD_REQUEST),
    ORDER_ITEM_LIST_EMPTY(1952, "Order must contain at least one item", HttpStatus.BAD_REQUEST),

    // ---------------- CATEGORY (2000–2299) ----------------

    // Not Found (2000–2049)
    CATEGORY_NOT_FOUND(2000, "Category not found", HttpStatus.NOT_FOUND),

    // Validation Errors (2050–2099)
    CATEGORY_NAME_REQUIRED(2050, "Category name is required", HttpStatus.BAD_REQUEST),
    CATEGORY_NAME_TOO_SHORT(2051, "Category name must be at least 2 characters", HttpStatus.BAD_REQUEST),
    CATEGORY_NAME_TOO_LONG(2052, "Category name must be at most 50 characters", HttpStatus.BAD_REQUEST),
    CATEGORY_ID_INVALID(2053, "Category ID is invalid", HttpStatus.BAD_REQUEST),
    CATEGORY_ALREADY_EXISTS(2054, "Category with this name already exists", HttpStatus.CONFLICT),

    // ---------------- REFRESH TOKEN (2300–2399) ----------------
    REFRESH_TOKEN_EXPIRED(2350, "Refresh token has expired", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INVALID(2351, "Refresh token is invalid", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_MISSING(2352, "Refresh token is missing", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_REUSED(2353, "Refresh token has already been used", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_NOT_FOUND(2354, "Refresh token not found", HttpStatus.UNAUTHORIZED);
    // ---------------- Fields ----------------

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
