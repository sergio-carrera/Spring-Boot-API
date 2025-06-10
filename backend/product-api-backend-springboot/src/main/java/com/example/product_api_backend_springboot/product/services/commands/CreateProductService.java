package com.example.product_api_backend_springboot.product.services.commands;

import com.example.product_api_backend_springboot.a_util.Command;
import com.example.product_api_backend_springboot.category.repository.CategoryRepository;
import com.example.product_api_backend_springboot.product.model.ProductRequest;
import com.example.product_api_backend_springboot.product.model.Product;
import com.example.product_api_backend_springboot.product.model.ProductDTO;
import com.example.product_api_backend_springboot.product.repository.ProductRepository;
import com.example.product_api_backend_springboot.product.validation.ProductValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CreateProductService implements Command<ProductRequest, ProductDTO>{

    private static final Logger logger = LoggerFactory.getLogger(CreateProductService.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductValidator productValidator;


    public CreateProductService(ProductRepository productRepository,
                                CategoryRepository categoryRepository,
                                ProductValidator productValidator) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productValidator = productValidator;
    }

    @Override
    public ResponseEntity<ProductDTO> execute(ProductRequest request) {
        logger.info("Executing " + getClass() + " product: " + request);

        Product productValidated = productValidator.execute(request, categoryRepository.findAll());
        productRepository.save(productValidated);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductDTO(productValidated));
    }
}
