package org.mrshim.transactionalservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {


    @org.springframework.web.bind.annotation.ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleOrderNotFoundException(OrderNotFoundException orderNotFoundException)
    {
        return new ResponseEntity<>(orderNotFoundException.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(OrderServiceException.class)
    public ResponseEntity<?> handleOrderServiceException(OrderServiceException orderServiceException)
    {
        return new ResponseEntity<>(orderServiceException.getMessage(),HttpStatus.SERVICE_UNAVAILABLE);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handlePaymentException(PaymentException paymentException)
    {
        return new ResponseEntity<>(paymentException.getMessage(),HttpStatus.SERVICE_UNAVAILABLE);
    }


}
