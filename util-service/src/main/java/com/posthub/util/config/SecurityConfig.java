package com.posthub.util.config;

import com.posthub.util.config.properties.EndpointsProperties;
import com.posthub.util.security.filter.JwtTokenFilter;
import com.posthub.util.security.model.IamServiceUserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final EndpointsProperties endpoints;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Swagger / OpenAPI
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-ui/**",
                                "/swagger-ui.html")
                        .permitAll()

                        // Preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ActionLogs secured endpoints
                        .requestMatchers(HttpMethod.GET,
                                endpoints.actionLogs() + endpoints.id()).hasAnyAuthority(allDashboardSecurityRoles())
                        .requestMatchers(HttpMethod.GET,
                                endpoints.actionLogs() + endpoints.all()).hasAnyAuthority(allDashboardSecurityRoles())
                        .requestMatchers(HttpMethod.PUT,
                                endpoints.actionLogs() + endpoints.markAsRead()).hasAnyAuthority(allDashboardSecurityRoles())

                        // Everything else denied
                        .anyRequest().denyAll()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private String[] allDashboardSecurityRoles() {
        return Arrays.stream(IamServiceUserRole.values())
                .map(Enum::name)
                .toArray(String[]::new);
    }
}
