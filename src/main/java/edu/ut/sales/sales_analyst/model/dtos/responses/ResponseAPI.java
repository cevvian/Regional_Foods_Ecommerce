package edu.ut.sales.sales_analyst.model.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResponseAPI<T> {
    private String responseMessage;
    private HttpStatus responseCode;
    private T data;

    public ResponseAPI(String responseMessage, HttpStatus responseCode, T data) {
        this.responseMessage = responseMessage;
        this.responseCode = responseCode;
        this.data = data;
    }
}
