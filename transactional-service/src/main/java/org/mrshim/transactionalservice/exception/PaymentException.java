package org.mrshim.transactionalservice.exception;

import lombok.Data;

import java.util.Date;

@Data
public class PaymentException extends RuntimeException{

    private String message;
    private Date timestamp;

    public PaymentException(String s) {

        this.message=s;
        this.timestamp=new Date();
    }
}
