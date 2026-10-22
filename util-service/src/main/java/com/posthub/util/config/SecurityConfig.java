package com.posthub.util.config;

import com.posthub.util.config.properties.EndpointsProperties;
import com.posthub.util.config.properties.SwaggerEndpointsProperties;
import com.posthub.util.security.filter.JwtTokenFilter;
import com.posthub.util.security.model.IamServiceUserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(EndpointsProperties.class)
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final EndpointsProperties endpoints;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        // Swagger / OpenAPI
                        .requestMatchers(SwaggerEndpointsProperties.permit()).permitAll()

                        // Preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ActionLogs secured endpoints
                        .requestMatchers(HttpMethod.GET, endpoints.permitGet()).hasAnyAuthority(allDashboardSecurityRoles())
                        .requestMatchers(HttpMethod.PUT, endpoints.permitPut()).hasAnyAuthority(allDashboardSecurityRoles())

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
