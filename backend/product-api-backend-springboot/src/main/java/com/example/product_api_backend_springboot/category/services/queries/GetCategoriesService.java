package com.example.product_api_backend_springboot.category.services.queries;

import com.example.product_api_backend_springboot.category.model.Category;
import com.example.product_api_backend_springboot.category.repository.CategoryRepository;
import com.example.product_api_backend_springboot.a_util.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetCategoriesService implements Query<Void, List<String>> {

    private static final Logger logger = LoggerFactory.getLogger(GetCategoriesService.class);

    private final CategoryRepository categoryRepository;

    public GetCategoriesService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Cacheable(value = "categoryCache", key = "'allCategories'")
    public ResponseEntity<List<String>> execute(Void input) {
        logger.info("Executing " + getClass());
        return ResponseEntity.ok(categoryRepository
                .findAll()
                .stream()
                .map(Category::getValue)
                .collect(Collectors.toList()));
    }
}
