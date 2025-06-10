package com.example.product_api_backend_springboot.product;

import com.example.product_api_backend_springboot.product.model.Product;
import com.example.product_api_backend_springboot.product.model.ProductDTO;
import com.example.product_api_backend_springboot.product.repository.ProductRepository;
import com.example.product_api_backend_springboot.product.services.queries.GetProductsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import utils.TestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetProductsServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private GetProductsService getProductsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void give_products_can_exist_get_products_service_return_list_of_product_dto() {
        //Given
        List<Product> mockProducts = TestUtils.getProducts();

        when(productRepository.findAll()).thenReturn(mockProducts);

        //When
        ResponseEntity<List<ProductDTO>> response = getProductsService.execute(null);

        //Then
        List<ProductDTO> expectedDtos = mockProducts.stream().map(ProductDTO::new).toList();
        assertEquals(expectedDtos, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void give_products_can_exist_get_products_service_return_empty_list() {
        //Given
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        //When
        ResponseEntity<List<ProductDTO>> response = getProductsService.execute(null);

        //Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(productRepository, times(1)).findAll();
    }
}
