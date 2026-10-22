package com.posthub.util.security.filter;

import com.posthub.util.security.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String jwt = authHeader.substring(BEARER_PREFIX.length());
            try {
                if (!jwtTokenProvider.validateToken(jwt)) {
                    log.warn("Invalid or expired token");
                    handleError(response, HttpStatus.UNAUTHORIZED, "Token is invalid or expired");
                    return;
                }

                setAuthentication(jwt);
            } catch (ExpiredJwtException e) {
                log.warn("JWT expired: {}", e.getMessage());
                handleError(response, HttpStatus.UNAUTHORIZED, "Token expired");
                return;
            } catch (SignatureException e) {
                log.warn("JWT signature invalid: {}", e.getMessage());
                handleError(response, HttpStatus.UNAUTHORIZED, "Invalid token signature");
                return;
            } catch (Exception e) {
                log.error("Unexpected JWT processing error: {}", e.getMessage(), e);
                handleError(response, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected token error");
                return;
            }
        } else {
            log.debug("No JWT token found in request headers");
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String jwt) {
        String username = jwtTokenProvider.getUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            List<String> roles = Optional.ofNullable(jwtTokenProvider.getRoles(jwt)).orElse(List.of());

            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            log.debug("Authenticated user: {}, with roles: {}", username, roles);
        }
    }

    private void handleError(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.getWriter().write(message);
    }
}