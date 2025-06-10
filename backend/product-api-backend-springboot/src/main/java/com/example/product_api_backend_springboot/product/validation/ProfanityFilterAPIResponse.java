package com.example.product_api_backend_springboot.product.validation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfanityFilterAPIResponse {
    private String original;
    private String censored;
    private boolean has_profanity;

    public ProfanityFilterAPIResponse(boolean has_profanity) {
        this.has_profanity = has_profanity;
    }
}
