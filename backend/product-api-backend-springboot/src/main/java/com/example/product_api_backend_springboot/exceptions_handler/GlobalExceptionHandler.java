package com.example.product_api_backend_springboot.exceptions_handler;

import com.example.product_api_backend_springboot.product.exceptions.InvalidProductException;
import com.example.product_api_backend_springboot.product.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNameIsRequired(InvalidProductException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST));
    }

}
