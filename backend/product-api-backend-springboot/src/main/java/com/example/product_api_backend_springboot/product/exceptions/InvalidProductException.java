package com.example.product_api_backend_springboot.product.exceptions;

import com.example.product_api_backend_springboot.exceptions_handler.ErrorMessages;
import com.example.product_api_backend_springboot.exceptions_handler.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class InvalidProductException extends RuntimeException{

    private static final Logger logger = LoggerFactory.getLogger(InvalidProductException.class);

    public InvalidProductException(String message) {
        super(message);
        logger.error("Exception: " + getClass() + " thrown");
    }
}
