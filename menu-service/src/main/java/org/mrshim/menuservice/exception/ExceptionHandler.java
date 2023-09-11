package org.mrshim.menuservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {


    @org.springframework.web.bind.annotation.ExceptionHandler(DishNotFoundException.class)
    public ResponseEntity<String> handleDishNotFound(DishNotFoundException ex)
    {

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);

    }






}
