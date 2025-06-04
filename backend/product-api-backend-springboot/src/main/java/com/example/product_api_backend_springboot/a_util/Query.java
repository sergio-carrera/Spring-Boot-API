package com.example.product_api_backend_springboot.a_util;

import org.springframework.http.ResponseEntity;

public interface Query <I, O> {
    ResponseEntity<O> execute(I input);
}
