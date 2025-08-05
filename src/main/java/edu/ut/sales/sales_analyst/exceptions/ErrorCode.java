package edu.ut.sales.sales_analyst.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    // ---------------- USER (1000–1299) ----------------

    // Not Found (1000–1049)
    USER_NOT_FOUND(1000, "USER not found", HttpStatus.NOT_FOUND),
    USER_PHONE_NOT_FOUND(1001, "USER's phone not found", HttpStatus.NOT_FOUND),
    USER_EMAIL_NOT_FOUND(1002, "USER's email not found", HttpStatus.NOT_FOUND),
    LIST_USER_NOT_FOUND(1003, "List of USERs not found", HttpStatus.NOT_FOUND),

    // Already Exists (1050–1074)
    USER_ALREADY_EXISTS(1050, "USER already exists", HttpStatus.CONFLICT),

    // Validation Errors (1075–1099)
    USER_NAME_REQUIRED(1075, "USER name is required", HttpStatus.BAD_REQUEST),
    USER_EMAIL_INVALID(1076, "Invalid email format", HttpStatus.BAD_REQUEST),
    USER_PHONE_INVALID(1077, "Invalid phone number format", HttpStatus.BAD_REQUEST),
    USER_ADDRESS_REQUIRED(1078, "Address cannot be blank", HttpStatus.BAD_REQUEST),

    // ---------------- PRODUCT (1300–1499) ----------------

    // Not Found (1300–1349)
    PRODUCT_NOT_FOUND(1300, "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_LIST_EMPTY(1301, "No products available", HttpStatus.NOT_FOUND),

    // Already Exists (1350–1374)
    PRODUCT_ALREADY_EXISTS(1350, "Product already exists", HttpStatus.CONFLICT),

    // Validation Errors (1375–1399)
    PRODUCT_INVALID_PRICE(1375, "Price must be greater than 0", HttpStatus.BAD_REQUEST),
    PRODUCT_INVALID_STOCK(1376, "Stock quantity cannot be negative", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(1377, "Invalid request parameters", HttpStatus.BAD_REQUEST),

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

    // Already Exists (2050–2074)
    CATEGORY_ALREADY_EXISTS(2050, "Category with this name already exists", HttpStatus.CONFLICT),

    // Validation Errors (2075–2099)
    CATEGORY_NAME_REQUIRED(2075, "Category name is required", HttpStatus.BAD_REQUEST),
    CATEGORY_NAME_TOO_SHORT(2076, "Category name must be at least 2 characters", HttpStatus.BAD_REQUEST),
    CATEGORY_NAME_TOO_LONG(2077, "Category name must be at most 50 characters", HttpStatus.BAD_REQUEST),
    CATEGORY_ID_INVALID(2078, "Category ID is invalid", HttpStatus.BAD_REQUEST),
    CATEGORY_REQUIRED(2079, "Category ID is required", HttpStatus.BAD_REQUEST),

    // ---------------- REFRESH TOKEN (2300–2349) ----------------
    REFRESH_TOKEN_EXPIRED(2300, "Refresh token has expired", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INVALID(2301, "Refresh token is invalid", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_MISSING(2302, "Refresh token is missing", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_REUSED(2303, "Refresh token has already been used", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_NOT_FOUND(2304, "Refresh token not found", HttpStatus.UNAUTHORIZED),

    // ---------------- NEWS (2400–2599) ----------------

    // Not Found (2400–2449)
    NEWS_NOT_FOUND(2400, "News not found", HttpStatus.NOT_FOUND),
    NEWS_LIST_EMPTY(2401, "No news articles found", HttpStatus.NOT_FOUND),

    // Already Exists (2450–2474)
    NEWS_ALREADY_EXISTS(2450, "News with this title already exists", HttpStatus.CONFLICT),

    // Validation Errors (2475–2499)
    NEWS_TITLE_REQUIRED(2475, "News title is required", HttpStatus.BAD_REQUEST),
    NEWS_TITLE_TOO_SHORT(2476, "News title must be at least 5 characters", HttpStatus.BAD_REQUEST),
    NEWS_TITLE_TOO_LONG(2477, "News title must be at most 150 characters", HttpStatus.BAD_REQUEST),
    NEWS_CONTENT_REQUIRED(2478, "News content is required", HttpStatus.BAD_REQUEST),
    NEWS_AUTHOR_REQUIRED(2479, "Author name is required", HttpStatus.BAD_REQUEST),
    NEWS_PUBLISH_DATE_INVALID(2480, "Publish date is invalid", HttpStatus.BAD_REQUEST),

    // ---------------- FILE / IMAGE UPLOAD (2600–2699) ----------------

    FILE_UPLOAD_NOT_FOUND(2600, "No file found to upload", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(2601, "Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_TYPE_NOT_SUPPORTED(2602, "File type is not supported", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FOLDER_INVALID(2603, "Target folder for upload is invalid", HttpStatus.BAD_REQUEST),

    // ---------------- Fields ----------------

    // ---------------- REVIEW (2700–2899) ----------------

    // Not Found (2700–2749)
    REVIEW_NOT_FOUND(2700, "Review not found", HttpStatus.NOT_FOUND),
    REVIEW_LIST_EMPTY(2701, "No reviews found", HttpStatus.NOT_FOUND),

    // Already Exists (2750–2774)
    REVIEW_ALREADY_EXISTS(2750, "Review already exists", HttpStatus.CONFLICT),

    // Validation Errors (2775–2799)
    REVIEW_RATING_INVALID(2775, "Rating must be between 1 and 5", HttpStatus.BAD_REQUEST),
    REVIEW_COMMENT_TOO_SHORT(2776, "Comment must be at least 5 characters", HttpStatus.BAD_REQUEST),
    REVIEW_COMMENT_TOO_LONG(2777, "Comment must be at most 1000 characters", HttpStatus.BAD_REQUEST),
    REVIEW_PRODUCT_ID_REQUIRED(2778, "Product ID is required for review", HttpStatus.BAD_REQUEST),
    REVIEW_USER_ID_REQUIRED(2779, "User ID is required for review", HttpStatus.BAD_REQUEST),

    UNAUTHORIZED_REVIEW(2780, "You can only review products you've purchased", HttpStatus.BAD_REQUEST),


    // ---------------- IMAGE NEWS (2900–2999) ----------------

    // Not Found (2900–2949)
    IMAGENEW_NOT_FOUND(2900, "Image not found", HttpStatus.NOT_FOUND),
    IMAGENEW_LIST_EMPTY(2901, "No images found", HttpStatus.NOT_FOUND),

    // Already Exists (2950–2969)
    IMAGENEW_ALREADY_EXISTS(2950, "Image already exists", HttpStatus.CONFLICT),

    // Validation Errors (2970–2999)
    IMAGENEW_TYPE_REQUIRED(2970, "Image type content is required", HttpStatus.BAD_REQUEST),
    IMAGENEW_URL_REQUIRED(2971, "Image URL is required", HttpStatus.BAD_REQUEST),
    IMAGENEW_NEWS_ID_REQUIRED(2972, "News ID for the image is required", HttpStatus.BAD_REQUEST),
    IMAGENEW_INVALID_FILE(2973, "Invalid image file", HttpStatus.BAD_REQUEST),

    // ---------------- REGION (3000–3099) ----------------

    // Not Found (3000-3049)
    REGION_NOT_FOUND(3000, "Region not found", HttpStatus.NOT_FOUND),
    REGION_LIST_EMPTY(3001, "No regions found", HttpStatus.NOT_FOUND),

    // Already Exists (3050-3069)
    REGION_ALREADY_EXISTS(3050, "Region already exists", HttpStatus.CONFLICT),

    // Validation Errors (3070-3099)
    REGION_NAME_REQUIRED(3003, "Region name is required", HttpStatus.BAD_REQUEST),

    // ---------------- CART (3100–3199) ----------------

    // Not Found (3100-3149)
    CART_NOT_FOUND(3100, "Cart not found", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND(3101, "Cart item not found", HttpStatus.NOT_FOUND),
    CART_EMPTY(1002, "Cart is empty", HttpStatus.NOT_FOUND),

    // Already Exists (3150-3169)
    CART_ALREADY_EXISTS(3150, "Cart already exists for this user", HttpStatus.CONFLICT),
    CART_ITEM_ALREADY_EXISTS(3151, "Item already exists in cart", HttpStatus.CONFLICT),

    // Validation Errors (3170-3199)
    INVALID_CART_ITEM_QUANTITY(3170, "Invalid quantity for cart item", HttpStatus.BAD_REQUEST),
    PRODUCT_OUT_OF_STOCK(3171, "Product is out of stock", HttpStatus.BAD_REQUEST),
    CART_ITEM_PRODUCT_MISMATCH(3172, "Cart item product does not match", HttpStatus.BAD_REQUEST),

    // ---------------- ADDRESS (3200–3299) ----------------

    // Not Found (3200-3249)
    ADDRESS_NOT_FOUND(3200, "Address not found", HttpStatus.NOT_FOUND),
    ADDRESS_LIST_EMPTY(3201, "No addresses found", HttpStatus.NOT_FOUND),

    // Already Exists (3250-3269)
    ADDRESS_ALREADY_EXISTS(3250, "Address already exists", HttpStatus.CONFLICT),

    // Validation Errors (3270-3299)
    ADDRESS_LINE_REQUIRED(3270, "Address line is required", HttpStatus.BAD_REQUEST),
    ADDRESS_CITY_REQUIRED(3271, "City is required", HttpStatus.BAD_REQUEST),
    ADDRESS_PROVINCE_REQUIRED(3272, "Province is required", HttpStatus.BAD_REQUEST),
    ADDRESS_PHONE_INVALID(3273, "Phone number is invalid", HttpStatus.BAD_REQUEST),
    CANNOT_DELETE_DEFAULT_ADDRESS(3274, "Cannot delete default address. Please change default address first.", HttpStatus.BAD_REQUEST)
    ;
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}
