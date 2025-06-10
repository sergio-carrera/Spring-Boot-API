package com.example.product_api_backend_springboot.product.validation;

import com.example.product_api_backend_springboot.configuration.API_KeyReader;
import com.example.product_api_backend_springboot.exceptions_handler.ErrorMessages;
import com.example.product_api_backend_springboot.product.exceptions.InvalidProductException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component //-> Permite ir al contenedor de Spring
public class ProfanityValidator {

    private static final Logger logger = LoggerFactory.getLogger(ProfanityValidator.class);

    private static final String API_KEY = API_KeyReader.getApiKey();

    private final RestTemplate restTemplate;

    public ProfanityValidator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean hasProfanity(String name, String description) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", API_KEY);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ProfanityFilterAPIResponse> responseEntity = restTemplate.exchange(
                    "https://api.api-ninjas.com/v1/profanityfilter?text=" + name + description,
                    HttpMethod.GET,
                    entity,
                    ProfanityFilterAPIResponse.class);


            logger.info("Profanity Validator: " + responseEntity.getBody());

            //maybe you want to log the username so you can know who is the one using curse words
            return (responseEntity.getBody().isHas_profanity());

        } catch (Exception e) {
            throw new InvalidProductException(ErrorMessages.PROFANITY_FILTER_DOWN.getMessage());
        }
    }
}
