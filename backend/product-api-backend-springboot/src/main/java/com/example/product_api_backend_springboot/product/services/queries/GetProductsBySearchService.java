package com.example.product_api_backend_springboot.product.services.queries;

import com.example.product_api_backend_springboot.a_util.Query;
import com.example.product_api_backend_springboot.product.model.GetProductsQuery;
import com.example.product_api_backend_springboot.product.model.Product;
import com.example.product_api_backend_springboot.product.model.ProductSortBy;
import com.example.product_api_backend_springboot.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.product_api_backend_springboot.product.model.ProductDTO;

@Service
public class GetProductsBySearchService implements Query<GetProductsQuery, List<ProductDTO>> {

    private final ProductRepository productRepository;

    private static final Logger logger = LoggerFactory.getLogger(GetProductsBySearchService.class);

    public GetProductsBySearchService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Cacheable(value = "productCache")
    public ResponseEntity<List<ProductDTO>> execute(GetProductsQuery query) {

        logger.info("GetProductsBySearchService query: {}", query);
        Sort productSort = defineSort(query.getProductSortBy());
        Pageable pageable = PageRequest.of(0, 5, productSort);

        List<Product> productList = productRepository.
                findByNameOrDescriptionAndRegionAndCategory(
                        query.getNameOrDescription(),
                        query.getRegion(),
                        query.getCategory(),
                        pageable
                );

        return ResponseEntity.ok(productList
                .stream()
                .map(ProductDTO::new)
                .toList()
        );
    }

    public Sort defineSort(ProductSortBy productSortBy) {
        if (productSortBy == null) {
            return Sort.unsorted();
        }

        ProductSortBy sortBy = ProductSortBy.valueOf(productSortBy.getValue());
        return Sort.by(String.valueOf(sortBy));
    }
}
