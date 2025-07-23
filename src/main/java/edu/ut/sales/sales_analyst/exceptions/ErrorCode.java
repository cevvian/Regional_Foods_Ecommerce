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
    CATEGORY_REQUIRED(2054, "Category ID is required", HttpStatus.BAD_REQUEST),
    CATEGORY_ALREADY_EXISTS(2055, "Category with this name already exists", HttpStatus.CONFLICT),
    // ---------------- Fields ----------------

    // ---------------- NEWS (2300–2599) ----------------

    // Not Found (2300–2349)
    NEWS_NOT_FOUND(2300, "News not found", HttpStatus.NOT_FOUND),
    NEWS_LIST_EMPTY(2301, "No news articles found", HttpStatus.NOT_FOUND),

    // Already Exists (2350–2399)
    NEWS_ALREADY_EXISTS(2350, "News with this title already exists", HttpStatus.CONFLICT),

    // Validation Errors (2400–2499)
    NEWS_TITLE_REQUIRED(2400, "News title is required", HttpStatus.BAD_REQUEST),
    NEWS_TITLE_TOO_SHORT(2401, "News title must be at least 5 characters", HttpStatus.BAD_REQUEST),
    NEWS_TITLE_TOO_LONG(2402, "News title must be at most 150 characters", HttpStatus.BAD_REQUEST),
    NEWS_CONTENT_REQUIRED(2403, "News content is required", HttpStatus.BAD_REQUEST),
    NEWS_AUTHOR_REQUIRED(2404, "Author name is required", HttpStatus.BAD_REQUEST),
    NEWS_PUBLISH_DATE_INVALID(2405, "Publish date is invalid", HttpStatus.BAD_REQUEST),

    // ---------------- FILE / IMAGE UPLOAD (2500–2599) ----------------

    FILE_UPLOAD_NOT_FOUND(2500, "No file found to upload", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(2501, "Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_TYPE_NOT_SUPPORTED(2502, "File type is not supported", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FOLDER_INVALID(2503, "Target folder for upload is invalid", HttpStatus.BAD_REQUEST);


    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }


}
