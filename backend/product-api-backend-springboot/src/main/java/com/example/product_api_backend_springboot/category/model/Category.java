package com.example.product_api_backend_springboot.category.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Category {
    //Haré que el valor de la Category sea la llave primaria
    @Id
    private String value;
}
