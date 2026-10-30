package com.gateway.filter;

import com.gateway.model.constants.ApiLogMessage;
import com.gateway.utils.ApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@Slf4j
public class LoggingRequestFilter implements GlobalFilter {

    @Value("${internal.gateway.service.name:post-hub-gateway}")
    private String internalGatewayName;
    @Value("${internal.header.name:post-hub-service-name}")
    private String internalHeaderName;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI uri = exchange.getRequest().getURI();
        log.info(ApiLogMessage.URL_IN_REQUEST.getMessage(), uri);

        ServerWebExchange webExchange = ApiUtils.addHeader(exchange, internalHeaderName, internalGatewayName);

        return chain.filter(webExchange);
    }

}
