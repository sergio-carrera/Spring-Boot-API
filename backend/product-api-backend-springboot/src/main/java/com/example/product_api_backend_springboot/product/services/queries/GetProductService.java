package com.example.product_api_backend_springboot.product.services.queries;

import com.example.product_api_backend_springboot.product.exceptions.ProductNotFoundException;
import com.example.product_api_backend_springboot.product.model.Product;
import com.example.product_api_backend_springboot.product.repository.ProductRepository;
import com.example.product_api_backend_springboot.a_util.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetProductService implements Query<String, Product> {

    private static final Logger logger = LoggerFactory.getLogger(GetProductService.class);

    private final ProductRepository productRepository;

    public GetProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ResponseEntity<Product> execute(String id) {
        logger.info("Executing " + getClass() + "input: " + id);
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            return ResponseEntity.ok(productOptional.get()); //-> Devuelvo el Product y no el ProductDTO
        }

        throw new ProductNotFoundException();
    }
}
