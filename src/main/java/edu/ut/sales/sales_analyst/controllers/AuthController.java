package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.components.JwtTokenUtils;
import edu.ut.sales.sales_analyst.mappers.UserMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.LoginRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.model.dtos.responses.TokenResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.UserResponse;
import edu.ut.sales.sales_analyst.model.entities.Token;
import edu.ut.sales.sales_analyst.model.entities.User;
import edu.ut.sales.sales_analyst.services.TokenService;
import edu.ut.sales.sales_analyst.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication", description = "APIs for authentication including login, register, logout, and token refresh")
public class AuthController {
    private final UserService userService;
    private final TokenService tokenService;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserMapper userMapper;

    @GetMapping("/generate-secret-key")
    public ResponseAPI<String> generateSecretKey(){
        String secretKey = jwtTokenUtils.generateSecretKey();
        return new ResponseAPI<>("Create secret key successfully", HttpStatus.CREATED, secretKey);
    }

    @PostMapping("/login")
    public ResponseAPI<TokenResponse> login(
            @RequestBody @Valid LoginRequest loginReq,
            HttpServletRequest request) {

        try {
            String token = userService.login(loginReq);
            User userDetail = userService.getUserDetailsFromToken(token);

            UserResponse userResponse = new UserResponse(
                    userDetail.getUserId(),
                    userDetail.getUserName(),
                    userDetail.getEmail(),
                    userDetail.getPhone(),
                    userDetail.getIsActive()
            );

            Token jwtToken = tokenService.addToken(userDetail, token);

            TokenResponse tokenResponse = new TokenResponse(
                    jwtToken.getTokenId(),
                    jwtToken.getToken(),
                    jwtToken.getRefreshToken(),
                    jwtToken.getTokenType(),
                    jwtToken.getExpirationDate(),
                    jwtToken.getRefreshExpirationDate(),
                    jwtToken.isRevoked(),
                    jwtToken.isExpired(),
                    userResponse
            );

            return new ResponseAPI<>("Đăng nhập thành công", HttpStatus.OK, tokenResponse);
        } catch (Exception ex) {
            return new ResponseAPI<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
