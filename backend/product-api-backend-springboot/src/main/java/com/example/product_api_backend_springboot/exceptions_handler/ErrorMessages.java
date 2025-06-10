package com.example.product_api_backend_springboot.exceptions_handler;

public enum ErrorMessages {
    PRODUCT_NOT_FOUND("Product Not Found"),
    PRODUCT_NAME_REQUIRED("Name is required"),
    PRODUCT_PRICE_CANNOT_BE_NEGATIVE("Price cannot be negative"),
    PRODUCT_REGION_NOT_FOUND("Region Not Found"),
    PRODUCT_CATEGORY_NOT_FOUND("Category Not Found"),
    PRODUCT_HAS_PROFANITY("Product cannot be saved due to explicit keywords"),
    PROFANITY_FILTER_DOWN("Profanity Filter external service is down");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
