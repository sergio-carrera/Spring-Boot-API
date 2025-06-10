package utils;

import com.example.product_api_backend_springboot.category.model.Category;
import com.example.product_api_backend_springboot.product.model.Product;
import com.example.product_api_backend_springboot.product.model.ProductRequest;
import com.example.product_api_backend_springboot.product.model.Region;
import com.example.product_api_backend_springboot.product.model.UpdateProductRequest;

import java.time.LocalDateTime;
import java.util.List;

public class TestUtils {
    public static List<Product> getProducts() {
        Product product1 = new Product();
        product1.setId("1");
        product1.setName("Dark souls 1");
        product1.setDescription("Best game");
        product1.setPrice(19.99);
        product1.setManufacturer("From Software");
        product1.setCategory(new Category("Games"));
        product1.setCreatedAt(LocalDateTime.now());
        product1.setUpdatedAt(LocalDateTime.now());
        product1.setRegion(Region.valueOf("US"));

        Product product2 = new Product();
        product2.setId("2");
        product2.setName("Dark souls 2");
        product2.setDescription("Game of the year");
        product2.setPrice(14.99);
        product2.setManufacturer("From Software");
        product2.setCategory(new Category("Games"));
        product2.setCreatedAt(LocalDateTime.now());
        product2.setUpdatedAt(LocalDateTime.now());
        product2.setRegion(Region.valueOf("CANADA"));

        return List.of(product1, product2);
    }

    public static Product getValidProduct() {
        Product product1 = new Product();
        product1.setId("1");
        product1.setName("Dark souls 1");
        product1.setDescription("Best game");
        product1.setPrice(19.99);
        product1.setManufacturer("From Software");
        product1.setCategory(new Category("Games"));
        product1.setCreatedAt(LocalDateTime.now());
        product1.setUpdatedAt(LocalDateTime.now());
        product1.setRegion(Region.valueOf("US"));

        return product1;
    }

    public static ProductRequest getValidProductRequest() {

        return new ProductRequest("Dark souls 1",
                "Best game",
                19.99,
                "From Software",
                "Electronics",
                "US"
        );

    }

    public static UpdateProductRequest getValidUpdateProductRequest() {
        return new UpdateProductRequest(
                "1",
                getValidProductRequest()
        );
    }

    public static List<Category> getCategories() {
        Category category1 = new Category();
        category1.setValue("Electronics");

        Category category2 = new Category();
        category2.setValue("Food");

        return List.of(category1, category2);
    }
}
