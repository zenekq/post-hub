package com.gateway.filter;

import com.gateway.service.RequestAuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServiceAuthorizationFilter extends AbstractGatewayFilterFactory<ServiceAuthorizationFilter.Config> {
    private final RequestAuthorizationService requestAuthorizationService;

    public ServiceAuthorizationFilter(@Lazy RequestAuthorizationService requestAuthorizationService) {
        super(Config.class);
        this.requestAuthorizationService = requestAuthorizationService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return requestAuthorizationService::validateAccessToService;
    }

        public static class Config {

        }

}

