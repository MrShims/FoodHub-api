package org.mrshim.orderservice.exception;

import lombok.Data;

import java.util.Date;

@Data
public class OrderNotFoundException extends RuntimeException{


    private String message;
    private Date timestamp;

    public OrderNotFoundException(String s) {

        this.message=s;
        this.timestamp=new Date();
    }
}
