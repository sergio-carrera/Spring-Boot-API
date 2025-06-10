package com.example.product_api_backend_springboot.product;

import com.example.product_api_backend_springboot.product.model.*;
import com.example.product_api_backend_springboot.product.repository.ProductRepository;
import com.example.product_api_backend_springboot.product.services.queries.GetProductsBySearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import utils.TestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetProductsBySearchServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private GetProductsBySearchService getProductsBySearchService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void give_product_dto_searched_by_parameters_does_not_exist_when_get_products_by_search_service_return_empty_list() {
        //Given
        //Se usa `any(Pageable.class)` para el argumento Pageable
        when(productRepository.findByNameOrDescriptionAndRegionAndCategory(
                isNull(), //isNull() para que Mockito sepa cómo comparar nulls
                isNull(),
                isNull(),
                any(Pageable.class)
        )).thenReturn(Collections.emptyList());

        //When
        ResponseEntity<List<ProductDTO>> response = getProductsBySearchService
                .execute(new GetProductsQuery(null, null, null, null));

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
        verify(productRepository, times(1))
                .findByNameOrDescriptionAndRegionAndCategory(isNull(), isNull(), isNull(), any(Pageable.class));
    }

    @Test
    public void give_product_dto_searched_by_parameters_exist_when_get_products_by_search_service_return_list_of_product_dto() {
        //Given
        List<Product> mockProducts = TestUtils.getProducts();
        when(productRepository.findByNameOrDescriptionAndRegionAndCategory(any(), any(), any(), any()))
                .thenReturn(mockProducts);

        GetProductsQuery query = new GetProductsQuery(null, null, null, null);

        //When
        ResponseEntity<List<ProductDTO>> response = getProductsBySearchService.execute(query);

        //Then
        List<ProductDTO> expectedDtos = mockProducts.stream()
                .map(ProductDTO::new)
                .toList();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockProducts.size(), response.getBody().size());
        assertEquals(expectedDtos, response.getBody());
        verify(productRepository, times(1))
                .findByNameOrDescriptionAndRegionAndCategory(any(), any(), any(), any());
    }

    @Test
    public void define_sort_returns_unsorted() {
        Sort sort = getProductsBySearchService.defineSort(null);
        assertEquals(sort, Sort.unsorted());
    }

    @Test
    public void define_sort_returns_sorted() {
        Sort sort = getProductsBySearchService.defineSort(ProductSortBy.name);
        assertEquals(sort, Sort.by(Sort.Direction.ASC, "name"));
    }
}

