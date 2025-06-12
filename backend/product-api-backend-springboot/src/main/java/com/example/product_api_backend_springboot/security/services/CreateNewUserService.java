package com.example.product_api_backend_springboot.security.services;

import com.example.product_api_backend_springboot.a_util.Command;
import com.example.product_api_backend_springboot.security.jwt.CustomUser;
import com.example.product_api_backend_springboot.security.repository.CustomUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CreateNewUserService implements Command<CustomUser, String> {

    private final PasswordEncoder encoder;

    private final CustomUserRepository customUserRepository;

    public CreateNewUserService(PasswordEncoder encoder, CustomUserRepository customUserRepository) {
        this.encoder = encoder;
        this.customUserRepository = customUserRepository;
    }

    @Override
    public ResponseEntity<String> execute(CustomUser user) {
        Optional<CustomUser> optionalUser = customUserRepository.findByUsername(user.getUsername());
        if (!optionalUser.isPresent()) {

            Set<String> defaultAuthorities = new HashSet<>();
            defaultAuthorities.add("ROLE_USER");

            customUserRepository.save(new CustomUser(user.getUsername(), encoder.encode(user.getPassword()), defaultAuthorities));
            return ResponseEntity.ok("Success");
        }

        return ResponseEntity.badRequest().body("Failure");
    }
}
