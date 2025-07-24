package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.components.JwtTokenUtils;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.services.TokenService;
import edu.ut.sales.sales_analyst.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication", description = "APIs for authentication including login, register, logout, and token refresh")
public class AuthController {
    UserService userService;
    TokenService tokenService;
    JwtTokenUtils jwtTokenUtils;

    @GetMapping("/generate-secret-key")
    public ResponseAPI<String> generateSecretKey(){
        String secretKey = jwtTokenUtils.generateSecretKey();
        return new ResponseAPI<>("Create secret key successfully", HttpStatus.CREATED, secretKey);
    }

}
