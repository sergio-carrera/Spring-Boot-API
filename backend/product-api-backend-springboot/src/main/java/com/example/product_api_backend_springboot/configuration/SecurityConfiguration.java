package com.example.product_api_backend_springboot.configuration;

import com.example.product_api_backend_springboot.security.jwt.JwtAuthenticationFilter;
import com.example.product_api_backend_springboot.security.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/login").permitAll(); //acceso sin autenticación
                    authorize.requestMatchers("/createnewuser").permitAll(); //acceso sin autenticación

                    //Permite solicitudes GET a /products/** sin autenticación
                    authorize.requestMatchers(HttpMethod.GET, "/products/**").permitAll();
                    authorize.requestMatchers(HttpMethod.GET, "/product/**").permitAll();
                    authorize.requestMatchers(HttpMethod.GET, "/product/search").permitAll();

                    authorize.anyRequest().authenticated(); //el resto necesita JWT
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(CustomUserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(userDetailsService);
    }
}
