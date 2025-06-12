package com.example.product_api_backend_springboot.security.services;

import com.example.product_api_backend_springboot.security.jwt.CustomUser;
import com.example.product_api_backend_springboot.security.repository.CustomUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomUserRepository customUserRepository;

    public CustomUserDetailsService(CustomUserRepository customUserRepository) {
        this.customUserRepository = customUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //can add logic to deal with the optional
        CustomUser customUser = customUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Convert the set of authority strings to GrantedAuthority objects
        Set<GrantedAuthority> grantedAuthorities = customUser.getAuthorities().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return User
                .withUsername(customUser.getUsername())
                .password(customUser.getPassword())
                .authorities(grantedAuthorities)
                .build();
    }
}