package com.example.product_api_backend_springboot.category;

import com.example.product_api_backend_springboot.category.model.Category;
import com.example.product_api_backend_springboot.category.services.GetCategoriesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    private final GetCategoriesService getCategoriesService;

    public CategoryController(GetCategoriesService getCategoriesService) {
        this.getCategoriesService = getCategoriesService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        return getCategoriesService.execute(null);
    }
}
