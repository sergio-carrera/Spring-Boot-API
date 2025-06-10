package com.example.product_api_backend_springboot.product.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ProductRequest {
    private String name;

    private String description;

    private Double price;

    private String manufacturer;

    private String category;

    private String region;
}
