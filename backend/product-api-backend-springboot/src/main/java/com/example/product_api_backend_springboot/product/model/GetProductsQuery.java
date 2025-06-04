package com.example.product_api_backend_springboot.product.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class GetProductsQuery {
    private Region region;
    private String category;
    private String nameOrDescription;
    private ProductSortBy productSortBy;

    public GetProductsQuery(Region region,
                            String category,
                            String nameOrDescription,
                            ProductSortBy productSortBy) {
        this.region = region;
        this.category = category;
        this.nameOrDescription = nameOrDescription;
        this.productSortBy = productSortBy;
    }
}
