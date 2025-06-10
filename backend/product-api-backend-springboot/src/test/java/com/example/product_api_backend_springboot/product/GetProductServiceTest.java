package com.example.product_api_backend_springboot.product;

import com.example.product_api_backend_springboot.product.exceptions.ProductNotFoundException;
import com.example.product_api_backend_springboot.product.model.Product;
import com.example.product_api_backend_springboot.product.repository.ProductRepository;
import com.example.product_api_backend_springboot.product.services.queries.GetProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import utils.TestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private GetProductService getProductService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void give_product_exists_when_get_product_service_return_product() {
        //Given
        Product product1 = TestUtils.getValidProduct();

        when(productRepository.findById("1")).thenReturn(Optional.of(product1));

        //When
        ResponseEntity<Product> response = getProductService.execute("1");

        //Then
        Optional<Product> expectedProduct = Optional.of(product1);
        assertEquals(expectedProduct.get(), response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productRepository, times(1)).findById("1");
    }

    @Test
    public void give_product_does_not_exist_when_get_product_service_return_product_not_found_exception() {
        //Given
        when(productRepository.findById("1")).thenReturn(Optional.empty());

        //When & Then
        assertThrows(ProductNotFoundException.class, () -> getProductService.execute("1"));
        verify(productRepository, times(1)).findById("1");
    }
}
