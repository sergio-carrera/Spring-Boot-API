package com.example.product_api_backend_springboot.product.repository;

import com.example.product_api_backend_springboot.product.model.Region;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.product_api_backend_springboot.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    //Se tuvo que usar @Query porque no se encontró la manera de hacerlo funcionar con Spring Data JPA
    @Query("SELECT p FROM Product p WHERE " +
            "(:nameOrDescription is null or p.name like %:nameOrDescription% or p.description like %:nameOrDescription%) and" +
            "(p.region = :region) and " +
            "(:category is null or p.category.value = :category)")
    List<Product> findByNameOrDescriptionAndRegionAndCategory(
            String nameOrDescription,
            Region region,
            String category,
            //Sort sort
            Pageable pageable
    );
}
