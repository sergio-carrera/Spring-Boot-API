package com.example.product_api_backend_springboot.product;

import com.example.product_api_backend_springboot.product.model.*;
import com.example.product_api_backend_springboot.product.services.commands.CreateProductService;
import com.example.product_api_backend_springboot.product.services.commands.DeleteProductService;
import com.example.product_api_backend_springboot.product.services.commands.UpdateProductService;
import com.example.product_api_backend_springboot.product.services.queries.GetProductService;
import com.example.product_api_backend_springboot.product.services.queries.GetProductsBySearchService;
import com.example.product_api_backend_springboot.product.services.queries.GetProductsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final GetProductsService getProductsService;
    private final GetProductService getProductService;
    private final GetProductsBySearchService getProductsBySearchService;
    private final CreateProductService createProductService;
    private final UpdateProductService updateProductService;
    private final DeleteProductService deleteProductService;

    public ProductController(GetProductsService getProductsService,
                             GetProductService getProductService,
                             GetProductsBySearchService getProductsBySearchService,
                             CreateProductService createProductService,
                             UpdateProductService updateProductService,
                             DeleteProductService deleteProductService) {
        this.getProductsService = getProductsService;
        this.getProductService = getProductService;
        this.getProductsBySearchService = getProductsBySearchService;
        this.createProductService = createProductService;
        this.updateProductService = updateProductService;
        this.deleteProductService = deleteProductService;
    }

    //Endpoint para obtener todos los ProductDTOs
    @GetMapping("/products")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ProductDTO>> getAllProductDTOs() {
        return getProductsService.execute(null);
    }

    //Endpoint para obtener un Product específico por ID
    @GetMapping("/product/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        return getProductService.execute(id);
    }

    //Endpoint para obtener una lista de ProductDTO por search del name / description
    @GetMapping("/product/search")
    @PreAuthorize("permitAll()")
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

    //Endpoint para crear un producto - requiere ser un usuario autenticado
    @PostMapping("/product")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SUPERUSER')")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductRequest productRequest) {
        return createProductService.execute(productRequest);
    }

    //Endpoint para actualizar un producto - requiere ser un usuario autenticado
    @PutMapping("/product/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SUPERUSER')")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable String id, @RequestBody ProductRequest productRequest) {
        return updateProductService.execute(new UpdateProductRequest(id, productRequest));
    }

    //Endpoint para crear un producto - requiere ser un usuario autenticado con rol de superusuario
    //(por ahora, lo asigno manualmente en la base de datos)
    @DeleteMapping("/product/{id}")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) { return deleteProductService.execute(id); }
}
