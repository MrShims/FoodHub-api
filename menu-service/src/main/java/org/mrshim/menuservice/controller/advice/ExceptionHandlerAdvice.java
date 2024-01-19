package org.mrshim.menuservice.controller.advice;


import org.mrshim.menuservice.exception.DishNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.mrshim.menuservice.utils.ExceptionUtil.extractErrorLocation;
import static org.mrshim.menuservice.utils.ExceptionUtil.extractErrorMessages;

/**
 * Класс для перехватывания ошибок
 */
@RestControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DishNotFoundException.class)
    public ResponseEntity<String> handleDishNotFound(DishNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage() + " " + extractErrorLocation(ex), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>(extractErrorMessages(ex.getBindingResult()), HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String errorMessage = "Http метод " + ex.getMethod() + " не поддерживается для этого метода. Поддерживаемые методы" +
                StringUtils.arrayToDelimitedString(ex.getSupportedMethods(), ", ");
        return new ResponseEntity<>(errorMessage, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
