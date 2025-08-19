package edu.ut.sales.sales_analyst.components;

import edu.ut.sales.sales_analyst.exceptions.JwtAuthenticationException;
import edu.ut.sales.sales_analyst.model.entities.Token;
import edu.ut.sales.sales_analyst.repositories.TokenRepo;
import edu.ut.sales.sales_analyst.security.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.GrantedAuthority;


@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    private final TokenRepo tokenRepository;

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.secretKey}")
    private String secretKey;

    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtils.class);

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof CustomUserDetails customUser) {
            claims.put("userId", customUser.getUser().getUserId());
            claims.put("roles", userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList()
            );

        }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername()) // username = email
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256-bit key
        random.nextBytes(keyBytes);
        return Encoders.BASE64.encode(keyBytes);
    }


    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String email = extractEmail(token);

            Token existingToken = tokenRepository.findByToken(token)
                    .orElseThrow(() -> new JwtAuthenticationException("Token not found in DB"));

            if (existingToken.isRevoked()) {
                throw new JwtAuthenticationException("Token is revoked");
            }

            if (!email.equals(userDetails.getUsername())) {
                throw new JwtAuthenticationException("Token email does not match user");
            }

            if (isTokenExpired(token)) {
                throw new JwtAuthenticationException("Token expired");
            }

            log.info("Token validation successful for {}", email);
            return true;
        } catch (JwtException e) {
            log.error("JWT validation error: {}", e.getMessage());
            throw new JwtAuthenticationException("Invalid JWT token");
        }
    }
}