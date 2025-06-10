package com.example.product_api_backend_springboot.product;

import com.example.product_api_backend_springboot.category.repository.CategoryRepository;
import com.example.product_api_backend_springboot.product.exceptions.ProductNotFoundException;
import com.example.product_api_backend_springboot.product.model.Product;
import com.example.product_api_backend_springboot.product.model.ProductDTO;
import com.example.product_api_backend_springboot.product.repository.ProductRepository;
import com.example.product_api_backend_springboot.product.services.commands.UpdateProductService;
import com.example.product_api_backend_springboot.product.validation.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.product_api_backend_springboot.product.model.UpdateProductRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import utils.TestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class UpdateProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductValidator productValidator;

    @InjectMocks
    private UpdateProductService updateProductService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void give_existing_product_with_valid_data_when_update_product_service_then_return_product_dto() {
        //Given
        UpdateProductRequest updateProductRequest = TestUtils.getValidUpdateProductRequest();
        Product existingProduct = TestUtils.getValidProduct();
        existingProduct.setId(updateProductRequest.getId()); //Importante: simular que el producto ya tiene un ID
        Product validatedProduct = TestUtils.getValidProduct(); //Producto después de la validación
        validatedProduct.setId(updateProductRequest.getId());
        validatedProduct.setCreatedAt(existingProduct.getCreatedAt()); //Mantener la fecha de creación original
        Product savedProduct = TestUtils.getValidProduct();

        when(productRepository.findById(updateProductRequest.getId())).thenReturn(Optional.of(existingProduct));
        when(productValidator.execute(any(), any())).thenReturn(validatedProduct);
        when(productRepository.save(any())).thenReturn(savedProduct);

        //When
        ResponseEntity<ProductDTO> response = updateProductService.execute(updateProductRequest);

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedProduct.getName(), response.getBody().getName());

        verify(productRepository, times(1)).findById(updateProductRequest.getId());
        verify(productValidator, times(1)).execute(any(), any());
        verify(productRepository, times(1)).save(validatedProduct); //Verifica que se guarda el producto *validado*
    }

    @Test
    public void give_non_exiting_product_with_valid_data_when_update_product_service_then_return_product_not_found_exception() {
        //Given
        UpdateProductRequest updateProductRequest = TestUtils.getValidUpdateProductRequest();
        when(productRepository.findById(updateProductRequest.getId())).thenReturn(Optional.empty());

        //When & Then
        assertThrows(ProductNotFoundException.class, () -> updateProductService.execute(updateProductRequest));

        verify(productRepository, times(1)).findById(updateProductRequest.getId());
        verify(productValidator, times(0)).execute(any(), any()); //El validador no debe ser llamado
        verify(productRepository, times(0)).save(any()); //El save no debe ser llamado
    }

    @Test
    public void give_existing_product_with_valid_data_when_update_product_service_then_product_validator_throws_run_time_exception() {
        //Given
        UpdateProductRequest updateProductRequest = TestUtils.getValidUpdateProductRequest();
        Product existingProduct = TestUtils.getValidProduct();
        existingProduct.setId(updateProductRequest.getId());

        when(productRepository.findById(updateProductRequest.getId())).thenReturn(Optional.of(existingProduct));
        //Simula que el validador lanza una excepción
        when(productValidator.execute(any(), any())).thenThrow(new RuntimeException("Validation failed"));

        //When & Then
        assertThrows(RuntimeException.class, () -> updateProductService.execute(updateProductRequest));

        verify(productRepository, times(1)).findById(updateProductRequest.getId());
        verify(productValidator, times(1)).execute(any(), any());
        verify(productRepository, times(0)).save(any()); //El save no debe ser llamado
    }
}
