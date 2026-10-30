package com.gateway.filter;

import com.gateway.service.RequestAuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InnerAuthFilter extends AbstractGatewayFilterFactory<InnerAuthFilter.Config> {
    private final RequestAuthorizationService requestAuthorizationService;

    public InnerAuthFilter(@Lazy RequestAuthorizationService requestAuthorizationService) {
        super(Config.class);
        this.requestAuthorizationService = requestAuthorizationService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return requestAuthorizationService::validateKeyHeader;
    }

    public static class Config {

    }

}