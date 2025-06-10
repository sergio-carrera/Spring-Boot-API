package com.example.product_api_backend_springboot.product;


import com.example.product_api_backend_springboot.exceptions_handler.ErrorMessages;
import com.example.product_api_backend_springboot.product.exceptions.InvalidProductException;
import com.example.product_api_backend_springboot.product.model.ProductRequest;
import com.example.product_api_backend_springboot.product.validation.ProductValidator;
import com.example.product_api_backend_springboot.product.validation.ProfanityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utils.TestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProductValidatorTest {

    @Mock
    private ProfanityValidator profanityValidator;

    @InjectMocks
    private ProductValidator productValidator;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testNameIsNull_throwsInvalidProductException() {
        ProductRequest productRequest = TestUtils.getValidProductRequest();
        productRequest.setName(null);
        InvalidProductException exception = assertThrows(InvalidProductException.class, () ->
                productValidator.execute(productRequest, TestUtils.getCategories()));
        assertEquals(exception.getMessage(), ErrorMessages.PRODUCT_NAME_REQUIRED.getMessage());
    }

    @Test
    public void testNameIsEmpty_throwsInvalidProductException() {
        ProductRequest productRequest = TestUtils.getValidProductRequest();
        productRequest.setName("");
        InvalidProductException exception = assertThrows(InvalidProductException.class, () ->
                productValidator.execute(productRequest, TestUtils.getCategories()));
        assertEquals(exception.getMessage(), ErrorMessages.PRODUCT_NAME_REQUIRED.getMessage());
    }

    @Test
    public void testPriceIsNegative_throwsInvalidProductException() {
        ProductRequest productRequest = TestUtils.getValidProductRequest();
        productRequest.setPrice(-9.99);
        InvalidProductException exception = assertThrows(InvalidProductException.class, () ->
                productValidator.execute(productRequest, TestUtils.getCategories()));
        assertEquals(exception.getMessage(), ErrorMessages.PRODUCT_PRICE_CANNOT_BE_NEGATIVE.getMessage());
    }

    @Test
    public void testCategoryIsNotAvailable_throwsInvalidProductException() {
        ProductRequest productRequest = TestUtils.getValidProductRequest();
        productRequest.setCategory("Not Available");
        InvalidProductException exception = assertThrows(InvalidProductException.class, () ->
                productValidator.execute(productRequest, TestUtils.getCategories()));
        assertEquals(exception.getMessage(), ErrorMessages.PRODUCT_CATEGORY_NOT_FOUND.getMessage());
    }

    @Test
    public void testCategoriesEmpty_throwsInvalidProductException() {
        ProductRequest productRequest = TestUtils.getValidProductRequest();
        InvalidProductException exception = assertThrows(InvalidProductException.class, () ->
                productValidator.execute(productRequest, Collections.emptyList()));
        assertEquals(exception.getMessage(), ErrorMessages.PRODUCT_CATEGORY_NOT_FOUND.getMessage());
    }

    @Test
    public void testRegionIsNotAvailable_throwsInvalidProductException() {
        ProductRequest productRequest = TestUtils.getValidProductRequest();
        productRequest.setRegion("Not Available");
        InvalidProductException exception = assertThrows(InvalidProductException.class, () ->
                productValidator.execute(productRequest, TestUtils.getCategories()));
        assertEquals(exception.getMessage(), ErrorMessages.PRODUCT_REGION_NOT_FOUND.getMessage());
    }

    @Test
    public void productHasProfanity_throwsInvalidProductException(){
        ProductRequest productRequest = TestUtils.getValidProductRequest();
        when(profanityValidator.hasProfanity(any(), any())).thenReturn(true);
        InvalidProductException exception = assertThrows(InvalidProductException.class, () ->
                productValidator.execute(productRequest, TestUtils.getCategories()));
        assertEquals(exception.getMessage(), ErrorMessages.PRODUCT_HAS_PROFANITY.getMessage());
    }
}
