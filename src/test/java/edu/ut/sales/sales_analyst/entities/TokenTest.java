package edu.ut.sales.sales_analyst.entities;

import edu.ut.sales.sales_analyst.model.entities.Token;
import edu.ut.sales.sales_analyst.model.entities.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TokenTest {

    @Test
    void testNoArgsConstructor() {
        Token token = new Token();
        assertNotNull(token);
    }

    @Test
    void testAllArgsConstructor() {
        String tokenId = "t123";
        String tokenValue = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
        String refreshToken = "refresh-abc-xyz";
        String tokenType = "Bearer";
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(15);
        LocalDateTime refreshExpiration = LocalDateTime.now().plusDays(7);
        boolean revoked = false;
        boolean expired = false;
        User user = new User();

        Token token = new Token(tokenId, tokenValue, refreshToken, tokenType,
                expiration, refreshExpiration, revoked, expired, user);

        assertEquals(tokenId, token.getTokenId());
        assertEquals(tokenValue, token.getToken());
        assertEquals(refreshToken, token.getRefreshToken());
        assertEquals(tokenType, token.getTokenType());
        assertEquals(expiration, token.getExpirationDate());
        assertEquals(refreshExpiration, token.getRefreshExpirationDate());
        assertFalse(token.isRevoked());
        assertFalse(token.isExpired());
        assertEquals(user, token.getUser());
    }

    @Test
    void testSuperBuilder() {
        LocalDateTime exp = LocalDateTime.now().plusMinutes(30);
        User user = new User();

        Token token = Token.builder()
                .tokenId("token-001")
                .token("access.jwt.token")
                .refreshToken("refresh.token")
                .tokenType("Bearer")
                .expirationDate(exp)
                .revoked(false)
                .expired(false)
                .user(user)
                .build();

        assertEquals("token-001", token.getTokenId());
        assertEquals("access.jwt.token", token.getToken());
        assertEquals("refresh.token", token.getRefreshToken());
        assertEquals("Bearer", token.getTokenType());
        assertEquals(exp, token.getExpirationDate());
        assertFalse(token.isRevoked());
        assertFalse(token.isExpired());
        assertEquals(user, token.getUser());
    }

    @Test
    void testGetterSetter() {
        Token token = new Token();

        token.setTokenId("tok001");
        token.setToken("abc.jwt.token");
        token.setRefreshToken("refresh.jwt.token");
        token.setTokenType("Bearer");
        token.setExpirationDate(LocalDateTime.of(2025, 8, 3, 15, 0));
        token.setRefreshExpirationDate(LocalDateTime.of(2025, 8, 10, 15, 0));
        token.setRevoked(true);
        token.setExpired(true);

        User user = new User();
        token.setUser(user);

        assertEquals("tok001", token.getTokenId());
        assertEquals("abc.jwt.token", token.getToken());
        assertEquals("refresh.jwt.token", token.getRefreshToken());
        assertEquals("Bearer", token.getTokenType());
        assertEquals(LocalDateTime.of(2025, 8, 3, 15, 0), token.getExpirationDate());
        assertEquals(LocalDateTime.of(2025, 8, 10, 15, 0), token.getRefreshExpirationDate());
        assertTrue(token.isRevoked());
        assertTrue(token.isExpired());
        assertEquals(user, token.getUser());
    }
}