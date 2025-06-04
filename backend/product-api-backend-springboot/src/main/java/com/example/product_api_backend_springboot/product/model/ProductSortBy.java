package com.example.product_api_backend_springboot.product.model;

public enum ProductSortBy {
    name("name"),
    price("price");

    private final String value;

    ProductSortBy(String value) {
        this.value = value;
    }

    //Se pasará un String y entrará en un bucle que valide el match con los valores esperados.
    public static ProductSortBy fromValue(String value) {
        for (ProductSortBy sortBy: ProductSortBy.values()) {
            if (sortBy.value.equalsIgnoreCase(value)) {
                return sortBy;
            }
        }
        return null;
    }

    public String getValue(){
        return this.value;
    }
}
