package com.example.product_api_backend_springboot.category;


import com.example.product_api_backend_springboot.category.model.Category;
import com.example.product_api_backend_springboot.category.repository.CategoryRepository;
import com.example.product_api_backend_springboot.category.services.queries.GetCategoriesService;
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

public class GetCategoriesServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private GetCategoriesService getCategoriesService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void give_categories_can_exist_get_categories_service_return_list_of_string_category() {
        //Given
        List<Category> categories = TestUtils.getCategories();

        when(categoryRepository.findAll()).thenReturn(categories);

        //When
        ResponseEntity<List<String>> response = getCategoriesService.execute(null);

        //Then
        List<String> expectedCategories = categories.stream().map(Category::getValue).toList();
        assertEquals(expectedCategories, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void give_categories_can_exist_get_categories_service_return_empty_list() {
        //Given
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        //When
        ResponseEntity<List<String>> response = getCategoriesService.execute(null);

        //Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(categoryRepository, times(1)).findAll();
    }
}
