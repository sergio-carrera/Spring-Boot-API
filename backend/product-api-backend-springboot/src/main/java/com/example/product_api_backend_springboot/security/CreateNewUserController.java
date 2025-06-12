package com.example.product_api_backend_springboot.security;


import com.example.product_api_backend_springboot.security.jwt.CustomUser;
import com.example.product_api_backend_springboot.security.services.CreateNewUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateNewUserController {

    private final CreateNewUserService createNewUserService;

    public CreateNewUserController(CreateNewUserService createNewUserService) {
        this.createNewUserService = createNewUserService;
    }

    @PostMapping("/createnewuser")
    public ResponseEntity<String> createNewUser(@RequestBody CustomUser user) {
        return createNewUserService.execute(user);
    }
}
