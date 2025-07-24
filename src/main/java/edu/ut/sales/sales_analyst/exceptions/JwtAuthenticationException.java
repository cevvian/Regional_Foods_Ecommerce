package edu.ut.sales.sales_analyst.exceptions;

public class JwtAuthenticationException extends RuntimeException {

    public JwtAuthenticationException(String message) {
        super(message);
    }

}