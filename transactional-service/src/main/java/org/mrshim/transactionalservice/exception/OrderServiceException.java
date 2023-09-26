package org.mrshim.transactionalservice.exception;

import lombok.Data;

import java.util.Date;

@Data
public class OrderServiceException extends RuntimeException{
    private String message;
    private Date timestamp;

    public OrderServiceException(String s) {

        this.message=s;
        this.timestamp=new Date();
    }
}
