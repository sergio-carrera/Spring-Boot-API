package com.example.product_api_backend_springboot.product;


import com.example.product_api_backend_springboot.product.exceptions.InvalidProductException;
import com.example.product_api_backend_springboot.product.validation.ProfanityFilterAPIResponse;
import com.example.product_api_backend_springboot.product.validation.ProfanityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ProfanityValidatorTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProfanityValidator profanityValidator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHasProfanity_returnsTrue() {
        //Given & When
        ProfanityFilterAPIResponse response = new ProfanityFilterAPIResponse(true);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(ProfanityFilterAPIResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        //Then
        assertTrue(profanityValidator.hasProfanity("testName", "testDescription"));
    }

    @Test
    public void testHasProfanity_returnsFalse() {
        //Given & When
        ProfanityFilterAPIResponse response = new ProfanityFilterAPIResponse(false);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(ProfanityFilterAPIResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        //Then
        assertFalse(profanityValidator.hasProfanity("testName", "testDescription"));
    }

    @Test
    public void testHasProfanity_throwsException() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(ProfanityFilterAPIResponse.class)))
                .thenThrow(new RuntimeException());

        assertThrows(InvalidProductException.class, () -> profanityValidator.hasProfanity("testName", "testDescription"));
    }
}
