package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.components.JwtTokenUtils;
import edu.ut.sales.sales_analyst.model.dtos.requests.RefreshTokenRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.LoginRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.model.dtos.responses.TokenResponse;
import edu.ut.sales.sales_analyst.model.entities.Token;
import edu.ut.sales.sales_analyst.model.entities.User;
import edu.ut.sales.sales_analyst.services.TokenService;
import edu.ut.sales.sales_analyst.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication", description = "APIs for authentication including login, logout, and token refresh")
public class AuthController {

    UserService userService;
    TokenService tokenService;
    JwtTokenUtils jwtTokenUtils;

    @Operation(summary = "Generate secret key", description = "Generate a new secret key for JWT signing")
    @GetMapping("/generate-secret-key")
    public ResponseAPI<String> generateSecretKey() {
        String secretKey = jwtTokenUtils.generateSecretKey();
        return new ResponseAPI<>("Create secret key successfully", HttpStatus.CREATED, secretKey);
    }

    @Operation(summary = "User login", description = "Authenticate user with username and password, returns JWT access and refresh tokens")
    @PostMapping("/login")
    public ResponseAPI<TokenResponse> login(
            @RequestBody @Valid LoginRequest loginReq,
            HttpServletRequest request) {

        try {
            String token = userService.login(loginReq);
            User userDetail = userService.getUserDetailsFromToken(token);

            Token jwtToken = tokenService.addToken(userDetail, token);

            TokenResponse tokenResponse = new TokenResponse(
                    jwtToken.getTokenId(),
                    jwtToken.getToken(),
                    jwtToken.getRefreshToken(),
                    jwtToken.getTokenType(),
                    jwtToken.getExpirationDate(),
                    jwtToken.getRefreshExpirationDate(),
                    jwtToken.isRevoked(),
                    jwtToken.isExpired()
            );

            return new ResponseAPI<>("Login successfully", HttpStatus.OK, tokenResponse);
        } catch (Exception ex) {
            return new ResponseAPI<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @Operation(summary = "Refresh access token", description = "Generate a new access token using a valid refresh token")
    @PostMapping("/refresh")
    public ResponseAPI<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest refreshReq) {
        try {
            User currentUser = userService.getUserFromRefreshToken(refreshReq.getRefreshToken());

            Token jwtToken = tokenService.refreshToken(refreshReq.getRefreshToken(), currentUser);

            TokenResponse tokenResponse = new TokenResponse(
                    jwtToken.getTokenId(),
                    jwtToken.getToken(),
                    jwtToken.getRefreshToken(),
                    jwtToken.getTokenType(),
                    jwtToken.getExpirationDate(),
                    jwtToken.getRefreshExpirationDate(),
                    jwtToken.isRevoked(),
                    jwtToken.isExpired()
            );

            return new ResponseAPI<>("Get refresh token successfully", HttpStatus.OK, tokenResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "User logout", description = "Invalidate the current access token to logout user")
    @PostMapping("/logout")
    public ResponseAPI<Boolean> logout(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseAPI<>("Missing or invalid Authorization header", HttpStatus.BAD_REQUEST, false);
            }

            String token = authHeader.substring(7);

            boolean revoked = tokenService.revokeToken(token);
            if (!revoked) {
                return new ResponseAPI<>("Token not found or already revoked", HttpStatus.BAD_REQUEST, false);
            }

            return new ResponseAPI<>("Logout successfully", HttpStatus.OK, true);
        } catch (Exception e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false);
        }
    }
}
