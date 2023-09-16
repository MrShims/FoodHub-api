package org.mrshim.orderservice.exception;

import lombok.Data;

import java.util.Date;


@Data
public class CreateOrderException extends RuntimeException {
    private String message;
    private Date timestamp;

    public CreateOrderException(String s) {

        this.message=s;
        this.timestamp=new Date();
    }
}

