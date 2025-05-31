package com.example.product_api_backend_springboot.Product.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) //JPA estándar para UUIDs
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "manufacturer")
    private String manufacturer;

    //Category
    @ManyToOne
    @JoinColumn(name = "category_name", referencedColumnName = "value")
    private Category category;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "region")
    @Enumerated(EnumType.STRING) //-> De esta manera cuando se salva en la base de datos, se guarda como un String
    private Region region;
}
