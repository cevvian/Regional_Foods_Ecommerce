package edu.ut.sales.sales_analyst.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    //----------------CUSTOMER--------------------//
    // Not Found (1000-1099)
    CUSTOMER_NOT_FOUND(1000, "Customer not found", HttpStatus.NOT_FOUND),
    CUSTOMER_PHONE_NOT_FOUND(1001, "Customer's phone not found", HttpStatus.NOT_FOUND),
    CUSTOMER_EMAIL_NOT_FOUND(1002, "Customer's email not found", HttpStatus.NOT_FOUND),
    lIST_CUSTOMER_NOT_FOUND(1003, "List customer not found", HttpStatus.NOT_FOUND),
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
