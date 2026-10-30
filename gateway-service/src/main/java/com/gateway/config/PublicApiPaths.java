package com.gateway.config;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PublicApiPaths {

    public List<String> getPublicPaths() {
        return List.of(
                "/auth/login",
                "/auth/register",
                "/auth/refresh/token",
                "/users/all",
                "/posts/all",
                "/comments/all",
                "/v3/api-docs",
                "/swagger-ui",
                "/swagger-ui.html",
                "/swagger-ui/index.html"
        );
    }
}
