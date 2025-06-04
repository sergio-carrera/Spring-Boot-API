package com.example.product_api_backend_springboot.category.repository;

import com.example.product_api_backend_springboot.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
}
