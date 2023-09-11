package org.mrshim.menuservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


@Data
public class DishNotFoundException extends RuntimeException {

    private String message;
    private Date timestamp;

    public DishNotFoundException(String s) {

        this.message=s;
        this.timestamp=new Date();
    }


}
