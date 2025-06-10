package com.example.product_api_backend_springboot.product.model;

import lombok.Getter;

@Getter
public class UpdateProductRequest {
    private String id;
    private ProductRequest productRequest;

    public UpdateProductRequest(String id, ProductRequest productRequest) {
        this.id = id;
        this.productRequest = productRequest;
    }
}
