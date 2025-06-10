package com.example.product_api_backend_springboot.product;

import com.example.product_api_backend_springboot.category.repository.CategoryRepository;
import com.example.product_api_backend_springboot.exceptions_handler.ErrorMessages;
import com.example.product_api_backend_springboot.product.exceptions.InvalidProductException;
import com.example.product_api_backend_springboot.product.model.Product;
import com.example.product_api_backend_springboot.product.model.ProductDTO;
import com.example.product_api_backend_springboot.product.model.ProductRequest;
import com.example.product_api_backend_springboot.product.repository.ProductRepository;
import com.example.product_api_backend_springboot.product.services.commands.CreateProductService;
import com.example.product_api_backend_springboot.product.validation.ProductValidator;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductValidator productValidator;

    @InjectMocks
    private CreateProductService createProductService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void give_product_request_entered_when_create_product_service_returns_product_dto() {
        //Given
        ProductRequest validProductRequest = TestUtils.getValidProductRequest();
        Product validatedProduct = TestUtils.getValidProduct(); //Un Product que el validador "devolvería"
        Product savedProduct = TestUtils.getValidProduct();

        //Configura el comportamiento de los mocks
        //Cuando productValidator.execute sea llamado con cualquier ProductRequest y cualquier List, devuelve validatedProduct
        when(productValidator.execute(any(ProductRequest.class), any(List.class))).thenReturn(validatedProduct);
        //Cuando productRepository.save sea llamado con cualquier Product, devuelve savedProduct
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);


        //When
        ResponseEntity<ProductDTO> response = createProductService.execute(validProductRequest);

        //Then
        //Verifica que la respuesta HTTP sea 201 Created
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        //Verifica que el cuerpo de la respuesta no sea nulo
        assertNotNull(response.getBody());
        //Verifica que el DTO devuelto coincida con el producto guardado
        assertEquals(savedProduct.getName(), response.getBody().getName());
        assertEquals(savedProduct.getPrice(), response.getBody().getPrice());

        //Verifica que los métodos de los mocks fueron llamados una vez
        verify(productValidator, times(1)).execute(any(ProductRequest.class), any(List.class));
        verify(productRepository, times(1)).save(any(Product.class));
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void give_product_request_entered_when_create_product_service_throws_invalid_product_exception() {
        //Given
        ProductRequest invalidProductRequest = TestUtils.getValidProductRequest(); // O un request inválido específico
        //Configuramos el mock del validador para que lance una excepción
        doThrow(new InvalidProductException(ErrorMessages.PRODUCT_NAME_REQUIRED.getMessage()))
                .when(productValidator)
                .execute(any(ProductRequest.class), any(List.class));

        //When & Then
        //Se espera que se lance InvalidProductException cuando se ejecute el servicio
        InvalidProductException exception = assertThrows(InvalidProductException.class, () ->
                createProductService.execute(invalidProductRequest));

        //Verificamos que el mensaje de la excepción (opcional, ya lo cubre ProductValidatorTest)
        assertEquals(ErrorMessages.PRODUCT_NAME_REQUIRED.getMessage(), exception.getMessage());

        //Verificamos que el validador fue llamado, pero el repositorio no (porque la validación falló)
        verify(productValidator, times(1)).execute(any(ProductRequest.class), any(List.class));
        verify(productRepository, times(0)).save(any(Product.class)); //El save nunca debería llamarse
        verify(categoryRepository, times(1)).findAll(); //findAll sí se llama antes de la validación
    }

    @Test
    public void give_product_request_entered_when_create_product_service_throws_exception(){
        //Given
        ProductRequest validProductRequest = TestUtils.getValidProductRequest();
        Product validatedProduct = TestUtils.getValidProduct();

        when(productValidator.execute(any(ProductRequest.class), any(List.class))).thenReturn(validatedProduct);
        //Se simula que el repositorio lanza una excepción al guardar (por ejemplo, por un problema de base de datos)
        doThrow(new RuntimeException("Database connection failed."))
                .when(productRepository)
                .save(any(Product.class));

        //When & Then
        //Se espera que se lance una RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                createProductService.execute(validProductRequest));

        assertEquals("Database connection failed.", exception.getMessage());

        //Se verifica que el validador fue llamado, y el repositorio también (antes de fallar)
        verify(productValidator, times(1)).execute(any(ProductRequest.class), any(List.class));
        verify(productRepository, times(1)).save(any(Product.class));
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void give_product_request_entered_when_create_product_service_category_repository_returns_empty_list_throws_invalid_product_exception(){
        //Given
        ProductRequest productRequest = TestUtils.getValidProductRequest();

        //Simula que CategoryRepository devuelve una lista vacía de categorías
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        //When & Then
        //Esperamos que el productValidator lance una InvalidProductException si no hay categorías
        doThrow(new InvalidProductException(ErrorMessages.PRODUCT_CATEGORY_NOT_FOUND.getMessage()))
                .when(productValidator)
                .execute(any(ProductRequest.class), any(List.class));

        InvalidProductException exception = assertThrows(InvalidProductException.class, () ->
                createProductService.execute(productRequest));

        assertEquals(ErrorMessages.PRODUCT_CATEGORY_NOT_FOUND.getMessage(), exception.getMessage());

        verify(categoryRepository, times(1)).findAll();
        // ProductValidator es llamado, pero productRepository.save no
        verify(productValidator, times(1)).execute(any(ProductRequest.class), any(List.class));
        verify(productRepository, times(0)).save(any(Product.class));
    }
}
