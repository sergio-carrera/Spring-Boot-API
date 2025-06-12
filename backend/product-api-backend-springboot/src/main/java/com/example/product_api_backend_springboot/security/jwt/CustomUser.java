package com.example.product_api_backend_springboot.security.jwt;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "custom_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomUser {
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    //Nuevo campo para las autoridades
    @ElementCollection(fetch = FetchType.EAGER) //Uso EAGER en vez de LAZY para cargar las autoridades inmediatamente para el 'UserDetailsService' y 'JwtAuthenticationFilter'
    @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "username"))
    @Column(name = "authority")
    private Set<String> authorities; //Para poder almacenar varios roles (un set para controlar que sean autoridades únicas)
}
