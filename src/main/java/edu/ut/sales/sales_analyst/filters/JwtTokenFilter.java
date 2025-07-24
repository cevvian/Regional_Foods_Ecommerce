package edu.ut.sales.sales_analyst.filters;

import edu.ut.sales.sales_analyst.components.JwtTokenUtils;
import edu.ut.sales.sales_analyst.model.entities.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtils jwtTokenUtil;

    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of("/api/v1/auth/generate-secret-key", "GET"),
                Pair.of("/api/v1/users/login", "POST"),
                Pair.of("/api/v1/users/register", "POST"),
                Pair.of("/swagger-ui/index.html", "GET"),
                Pair.of("/v3/api-docs", "GET"),
                Pair.of("/v3/api-docs/**", "GET"),
                Pair.of("/v3/api-docs/swagger-config", "GET"),
                Pair.of("/swagger-ui/**", "GET"),
                Pair.of("/swagger-ui.html", "GET")
        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        return bypassTokens.stream()
                .anyMatch(token -> requestPath.startsWith(token.getFirst()) &&
                        requestMethod.equalsIgnoreCase(token.getSecond()));
    }
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (isBypassToken(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            final String authHeader = request.getHeader("Authorization");

            // If no auth header or not a Bearer token, let Spring Security handle it
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Extract and validate token
            final String token = authHeader.substring(7);
            final String email = jwtTokenUtil.extractEmail(token);

            // If we have an email and no authentication exists yet, validate the token
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetails = (User) userDetailsService.loadUserByUsername(email);

                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    // Create authentication token
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set authentication in context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            // Continue filter chain
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // Let the exception propagate to the authentication entry point
            filterChain.doFilter(request, response);
        }

    }
}
