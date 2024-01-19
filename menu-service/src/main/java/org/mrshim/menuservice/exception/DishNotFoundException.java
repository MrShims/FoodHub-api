package org.mrshim.menuservice.exception;

import lombok.Data;

import java.util.Date;


/**
 * Класс Exception если блюдо не найдено
 */
@Data
public class DishNotFoundException extends RuntimeException {

    private String message;
    private Date timestamp;

    public DishNotFoundException(String s) {

        this.message = s;
        this.timestamp = new Date();
    }
}
