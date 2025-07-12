package edu.ut.sales.sales_analyst.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    //----------------CUSTOMER--------------------//

    // Not Found (1000–1099)
    CUSTOMER_NOT_FOUND(1000, "Customer not found", HttpStatus.NOT_FOUND),
    CUSTOMER_PHONE_NOT_FOUND(1001, "Customer's phone not found", HttpStatus.NOT_FOUND),
    CUSTOMER_EMAIL_NOT_FOUND(1002, "Customer's email not found", HttpStatus.NOT_FOUND),
    LIST_CUSTOMER_NOT_FOUND(1003, "List of customers not found", HttpStatus.NOT_FOUND),

    // Already Exists (1100–1199)
    CUSTOMER_ALREADY_EXISTS(1100, "Customer already exists", HttpStatus.CONFLICT),

    // Validation Errors (1200–1299)
    CUSTOMER_NAME_REQUIRED(1200, "Customer name is required", HttpStatus.BAD_REQUEST),
    CUSTOMER_EMAIL_INVALID(1201, "Invalid email format", HttpStatus.BAD_REQUEST),
    CUSTOMER_PHONE_INVALID(1202, "Invalid phone number format", HttpStatus.BAD_REQUEST),
    CUSTOMER_ADDRESS_REQUIRED(1203, "Address cannot be blank", HttpStatus.BAD_REQUEST),


    //---------------PRODUCT---------------------//
    // Not Found (2000–2099)
    PRODUCT_NOT_FOUND(2000, "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_LIST_EMPTY(2001, "No products available", HttpStatus.NOT_FOUND),

    // Already Exists (2100–2199)
    PRODUCT_ALREADY_EXISTS(2100, "Product already exists", HttpStatus.CONFLICT),

    // Validation Errors (2200–2299)
    PRODUCT_INVALID_PRICE(2200, "Price must be greater than 0", HttpStatus.BAD_REQUEST),
    PRODUCT_INVALID_STOCK(2201, "Stock quantity cannot be negative", HttpStatus.BAD_REQUEST),
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
