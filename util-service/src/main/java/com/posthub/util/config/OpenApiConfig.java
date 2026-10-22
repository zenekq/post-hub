package com.posthub.util.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "'POST_HUB' REST API",
                version = "1.0",
                description = "UTILS-service, REST API",
                contact = @Contact(name = "POST_HUB")),
        security = { @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)}
)
@SecurityScheme(
        name = HttpHeaders.AUTHORIZATION,
        type = SecuritySchemeType.HTTP,
        scheme = "Bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {

    @Value("${swagger.servers.first:#{null}}")
    private String firstServer;

    @Value("${swagger.servers.second:#{null}}")
    private String secondServer;

    @Bean
    public GroupedOpenApi utilsApi() {
        return GroupedOpenApi.builder()
                .group("utils-service")
                .packagesToScan("com.posthub.util.controller")
                .addOpenApiCustomizer(serverCustomizer())
                .build();
    }

    @Bean
    public OpenApiCustomizer serverCustomizer() {
        return openApi -> {
            List<Server> servers = new ArrayList<>();
            if (Objects.nonNull(firstServer)) {
                servers.add(new Server().url(firstServer).description("API Server"));
            }
            if (Objects.nonNull(secondServer)) {
                servers.add(new Server().url(secondServer).description("Second API Server"));
            }
            openApi.setServers(servers);
        };
    }
}