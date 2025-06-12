package com.example.product_api_backend_springboot.security.repository;

import com.example.product_api_backend_springboot.security.jwt.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface CustomUserRepository extends JpaRepository<CustomUser, String> {
    Optional<CustomUser> findByUsername(String username);
}
