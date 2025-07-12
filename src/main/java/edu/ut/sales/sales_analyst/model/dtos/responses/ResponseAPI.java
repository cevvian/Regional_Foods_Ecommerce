package edu.ut.sales.sales_analyst.model.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResponseAPI<T> {
    private String message;
    private HttpStatus code;
    private T data;

    public ResponseAPI(String responseMessage, HttpStatus responseCode, T data) {
        this.message = responseMessage;
        this.code = responseCode;
        this.data = data;
    }
}
