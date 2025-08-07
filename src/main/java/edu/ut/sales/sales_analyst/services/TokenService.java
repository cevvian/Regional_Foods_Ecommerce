package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.components.JwtTokenUtils;
import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.model.entities.Token;
import edu.ut.sales.sales_analyst.model.entities.User;
import edu.ut.sales.sales_analyst.repositories.TokenRepo;
import edu.ut.sales.sales_analyst.repositories.UserRepo;
import edu.ut.sales.sales_analyst.security.CustomUserDetails;
import edu.ut.sales.sales_analyst.services.impl.ITokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {
    private static final int MAX_TOKENS = 3;
    private final UserRepo userRepo;

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    private final TokenRepo tokenRepository;

    private final JwtTokenUtils jwtTokenUtil;
    @Override
    public Token addToken(User user, String token) {
        User existingUser = userRepo.findByUserId(user.getUserId());
        List<Token> userTokens = tokenRepository.findByUser_userId(user.getUserId());
        int tokenCount = userTokens.size();

        if (tokenCount >= MAX_TOKENS) {
            Token tokenToDelete = userTokens.get(0);
            tokenRepository.delete(tokenToDelete);
        }

        long expirationInSeconds = expiration;
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expirationInSeconds);

        Token newToken = Token.builder()
                .user(existingUser)
                .token(token)
                .revoked(false)
                .expired(false)
                .tokenType("Bearer")
                .expirationDate(expirationDateTime)
                .build();

        newToken.setRefreshToken(UUID.randomUUID().toString());
        newToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        tokenRepository.save(newToken);
        return newToken;
    }

    @Override
    @Transactional
    public Token refreshToken(String refreshToken, User user) throws Exception {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (existingToken.getRefreshExpirationDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(existingToken);
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        UserDetails userDetails = new CustomUserDetails(user);

        // Generate new access token
        String newAccessToken = jwtTokenUtil.generateToken(userDetails);
        LocalDateTime accessExp = LocalDateTime.now().plusSeconds(expiration);

        // Rotate refresh token
        String newRefreshToken = UUID.randomUUID().toString();
        LocalDateTime refreshExp = LocalDateTime.now().plusSeconds(expirationRefreshToken);

        // Update DB
        existingToken.setToken(newAccessToken);
        existingToken.setExpirationDate(accessExp);
        existingToken.setRefreshToken(newRefreshToken);
        existingToken.setRefreshExpirationDate(refreshExp);

        return tokenRepository.save(existingToken);
    }

    @Override
    public Boolean revokeToken(String token) {
        Token existingToken = tokenRepository.findByToken(token)
                .orElse(null);

        if (existingToken == null || existingToken.isRevoked()) {
            return false;
        }

        existingToken.setRevoked(true);
        tokenRepository.save(existingToken);
        return true;
    }

}
