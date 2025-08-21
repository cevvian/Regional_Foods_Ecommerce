package edu.ut.sales.sales_analyst.model.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResponseAPI<T> {
    private String message;
    private int code;
    private T data;
    private Object meta;

    public ResponseAPI(String message, HttpStatus status) {
        this.message = message;
        this.code = status.value();
        this.data = null;
    }

    public ResponseAPI(String responseMessage, HttpStatus responseCode, T data) {
        this.message = responseMessage;
        this.code = responseCode.value();
        this.data = data;
    }

    public ResponseAPI(String message, HttpStatus status, T data, Object meta) {
        this.message = message;
        this.code = status.value();
        this.data = data;
        this.meta = meta;
    }
}
