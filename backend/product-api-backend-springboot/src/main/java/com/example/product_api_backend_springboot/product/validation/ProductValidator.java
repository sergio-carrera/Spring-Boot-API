package com.example.product_api_backend_springboot.product.validation;


import com.example.product_api_backend_springboot.category.model.Category;
import com.example.product_api_backend_springboot.exceptions_handler.ErrorMessages;
import com.example.product_api_backend_springboot.product.exceptions.InvalidProductException;
import com.example.product_api_backend_springboot.product.model.Product;
import com.example.product_api_backend_springboot.product.model.ProductRequest;
import com.example.product_api_backend_springboot.product.model.Region;
import com.mysql.cj.util.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductValidator {

    private final ProfanityValidator profanityValidator;

    public ProductValidator(ProfanityValidator profanityValidator) {
        this.profanityValidator = profanityValidator;
    }

    public Product execute(ProductRequest request, List<Category> availableCategories) {
        if (nameIsEmpty(request.getName())) {
            throw new InvalidProductException(ErrorMessages.PRODUCT_NAME_REQUIRED.getMessage());
        }

        if(priceIsNegative(request.getPrice())) {
            throw new InvalidProductException(ErrorMessages.PRODUCT_PRICE_CANNOT_BE_NEGATIVE.getMessage());
        }

        if(categoryNotAvailable(request.getCategory(), availableCategories)) {
            throw new InvalidProductException(ErrorMessages.PRODUCT_CATEGORY_NOT_FOUND.getMessage());
        }

        if(regionNotAvailable(request.getRegion())) {
            throw new InvalidProductException(ErrorMessages.PRODUCT_REGION_NOT_FOUND.getMessage());
        }

        if(profanityValidator.hasProfanity(request.getName(), request.getDescription())) {
            throw new InvalidProductException(ErrorMessages.PRODUCT_HAS_PROFANITY.getMessage());
        }

        return new Product(request);
    }

    private static boolean nameIsEmpty(String name) {
        return StringUtils.isNullOrEmpty(name);
    }

    private static boolean priceIsNegative(Double price) {
        return price != null && price < 0.00;
    }

    private static boolean categoryNotAvailable(String category, List<Category> availableCategories) {
        return !availableCategories.contains(new Category(category));
    }

    private static boolean regionNotAvailable(String candidateRegion) {
        for (Region region: Region.values()) {
            if (region.name().equals(candidateRegion)) {
                return false;
            }
        }
        return true;
    }
}
