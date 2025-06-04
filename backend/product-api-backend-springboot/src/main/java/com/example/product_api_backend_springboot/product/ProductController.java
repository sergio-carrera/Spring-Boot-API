package com.example.product_api_backend_springboot.product;

import com.example.product_api_backend_springboot.product.model.*;
import com.example.product_api_backend_springboot.product.services.GetProductService;
import com.example.product_api_backend_springboot.product.services.GetProductsBySearchService;
import com.example.product_api_backend_springboot.product.services.GetProductsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final GetProductsService getProductsService;
    private final GetProductService getProductService;
    private final GetProductsBySearchService getProductsBySearchService;

    public ProductController(GetProductsService getProductsService, GetProductService getProductService, GetProductsBySearchService getProductsBySearchService) {
        this.getProductsService = getProductsService;
        this.getProductService = getProductService;
        this.getProductsBySearchService = getProductsBySearchService;
    }

    //Endpoint para obtener todos los ProductDTOs
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProductDTOs() {
        return getProductsService.execute(null);
    }

    //Endpoint para obtener un Product específico por ID
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        return getProductService.execute(id);
    }

    //Endpoint para obtener una lista de ProductDTO por search del name / description
    @GetMapping("/product/search")
    public ResponseEntity<List<ProductDTO>> searchProduct(
            @RequestHeader(value = "region", defaultValue = "US") String region,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String nameOrDescription,
            @RequestParam(required = false) String orderBy
    ){
        return getProductsBySearchService.execute(new GetProductsQuery(Region.valueOf(region),
                category,
                nameOrDescription,
                ProductSortBy.fromValue(orderBy)
                ));
    }
}
