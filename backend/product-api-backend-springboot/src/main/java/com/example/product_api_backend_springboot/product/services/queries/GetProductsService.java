package com.example.product_api_backend_springboot.product.services.queries;

import com.example.product_api_backend_springboot.product.model.ProductDTO;
import com.example.product_api_backend_springboot.product.repository.ProductRepository;
import com.example.product_api_backend_springboot.a_util.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetProductsService implements Query<Void, List<ProductDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(GetProductsService.class);

    private final ProductRepository productRepository;

    public GetProductsService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ResponseEntity<List<ProductDTO>> execute(Void input) {
        logger.info("Executing " + getClass());
        return ResponseEntity.ok(productRepository.findAll().stream().map(ProductDTO::new).toList());
    }
}
