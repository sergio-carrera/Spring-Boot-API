package com.example.product_api_backend_springboot.product;

import com.example.product_api_backend_springboot.product.exceptions.ProductNotFoundException;
import com.example.product_api_backend_springboot.product.model.Product;
import com.example.product_api_backend_springboot.product.repository.ProductRepository;
import com.example.product_api_backend_springboot.product.services.commands.DeleteProductService;
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

import static org.mockito.Mockito.when;

public class DeleteProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private DeleteProductService deleteProductService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void give_existing_product_when_delete_product_service_return_void() {
        //Given
        String productId = "someExistingId";
        Product existingProduct = TestUtils.getValidProduct();
        existingProduct.setId(productId);

        //Cuando se llame a findById con el ID, devuelve un Optional con el producto.
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        //Cuando se llame a deleteById, no hace nada (comportamiento por defecto de un mock void, pero explícito es bueno).
        doNothing().when(productRepository).deleteById(productId);

        //When
        ResponseEntity<Void> response = deleteProductService.execute(productId);

        //Then
        //Verifica que la respuesta HTTP sea 204 NO_CONTENT
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        //Verifica que el cuerpo de la respuesta sea nulo (es un ResponseEntity<Void>)
        assertNull(response.getBody());

        //Verifica que findById fue llamado exactamente una vez con el ID correcto
        verify(productRepository, times(1)).findById(productId);
        //Verifica que deleteById fue llamado exactamente una vez con el ID correcto
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test public void give_non_existing_product_when_delete_product_service_throws_product_not_found_exception() {
        //Given
        String nonExistingProductId = "nonExistingId";

        //Cuando se llame a findById con el ID, devuelve un Optional vacío
        when(productRepository.findById(nonExistingProductId)).thenReturn(Optional.empty());

        //When & Then
        //Espera que se lance ProductNotFoundException
        assertThrows(ProductNotFoundException.class, () ->
                deleteProductService.execute(nonExistingProductId));

        //Verifica que findById fue llamado exactamente una vez
        verify(productRepository, times(1)).findById(nonExistingProductId);
        //Verifica que deleteById *nunca* fue llamado, ya que el producto no se encontró
        verify(productRepository, times(0)).deleteById(nonExistingProductId);
    }
}
