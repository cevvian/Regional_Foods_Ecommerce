package edu.ut.sales.sales_analyst.filters;

import edu.ut.sales.sales_analyst.components.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtils jwtTokenUtil;

    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of("/api/v1/auth/generate-secret-key", "GET"),
                Pair.of("/api/v1/auth/login", "POST"),
                Pair.of("/api/v1/users/register", "POST"),
                Pair.of("/api/v1/users/refresh-token", "POST"),
                Pair.of("/swagger-ui/index.html", "GET"),
                Pair.of("/v3/api-docs", "GET"),
                Pair.of("/v3/api-docs/swagger-config", "GET"),
                Pair.of("/swagger-ui.html", "GET"),
                Pair.of("/ws/info", "GET"),
                Pair.of("/api/v1/news", "GET"),
                Pair.of("/api/v1/news/{id}", "GET"),
                Pair.of("/api/v1/products", "GET"),
                Pair.of("/api/v1/products/{id}", "GET"),
                Pair.of("/api/v1/products/filter", "GET"),
                Pair.of("/api/v1/reviews", "GET"),
                Pair.of("/api/v1/reviews/{id}", "GET"),
                Pair.of("/api/v1/reviews/by-product", "GET"),
                Pair.of("/api/v1/passwords/send-otp", "GET"),
                Pair.of("/api/v1/passwords/send-otp", "POST"),
                Pair.of("/api/v1/passwords/verify-otp", "POST"),
                Pair.of("/api/v1/passwords/forgot-password", "POST")
        );

        String path = request.getServletPath();
        String method = request.getMethod();

        return bypassTokens.stream()
                .anyMatch(token -> path.startsWith(token.getFirst()) &&
                        method.equalsIgnoreCase(token.getSecond()));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Check bypass
        if (isBypassToken(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Get Authorization header
            final String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.trim().startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Extract token
            final String token = authHeader.trim().substring(7).trim();
            final String email = jwtTokenUtil.extractEmail(token);

            // Validate token & set authentication
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    log.warn("Token validation failed for {}", email);
                }
            }
        } catch (Exception e) {
            log.error("Exception in JwtTokenFilter: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }
}
