package org.mrshim.menuservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Класс для перехватывания ошибок
 */
@ControllerAdvice
public class ExceptionHandler {


    /**
     * Метод для перехватывания DishNotFoundException
     *
     * @param ex содержание ошибки
     * @return ResponseEntity содержащий cообщение об ошибке и HttpStatus.NOT_FOUND
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(DishNotFoundException.class)
    public ResponseEntity<String> handleDishNotFound(DishNotFoundException ex)
    {

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);

    }






}
