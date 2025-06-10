package com.example.product_api_backend_springboot.product.services.commands;

import com.example.product_api_backend_springboot.a_util.Command;
import com.example.product_api_backend_springboot.category.repository.CategoryRepository;
import com.example.product_api_backend_springboot.product.exceptions.ProductNotFoundException;
import com.example.product_api_backend_springboot.product.model.Product;
import com.example.product_api_backend_springboot.product.model.ProductDTO;
import com.example.product_api_backend_springboot.product.model.ProductRequest;
import com.example.product_api_backend_springboot.product.model.UpdateProductRequest;
import com.example.product_api_backend_springboot.product.repository.ProductRepository;
import com.example.product_api_backend_springboot.product.validation.ProductValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateProductService implements Command<UpdateProductRequest, ProductDTO> {

    private static final Logger logger = LoggerFactory.getLogger(UpdateProductService.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductValidator productValidator;

    public UpdateProductService(ProductRepository productRepository,
                                CategoryRepository categoryRepository,
                                ProductValidator productValidator) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productValidator = productValidator;
    }

    @Override
    public ResponseEntity<ProductDTO> execute(UpdateProductRequest input) {
        logger.info("Executing " + getClass() + " input: " + input);

        Optional<Product> productOptional = productRepository.findById(input.getId());

        if (productOptional.isPresent()){
            Product productValidated = productValidator.execute(input.getProductRequest(), categoryRepository.findAll());
            productValidated.setId(productOptional.get().getId());
            productValidated.setCreatedAt(productOptional.get().getCreatedAt());
            productRepository.save(productValidated);
            return ResponseEntity.ok(new ProductDTO(productValidated));
        }

        throw new ProductNotFoundException();
    }
}
