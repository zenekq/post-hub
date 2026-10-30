package com.gateway.filter;

import com.gateway.model.constants.ApiLogMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Slf4j
public class LoggingResponseFilter implements GlobalFilter {
    private static final String LOGIN_END_POINT = "/auth/login";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info(ApiLogMessage.URL_IN_RESPONSE.getMessage(), exchange.getRequest().getURI());
        if (exchange.getRequest().getPath().toString().contains(LOGIN_END_POINT)) {
            HttpStatus statusCode = HttpStatus.resolve(exchange.getResponse().getStatusCode().value());
            String logMessage = Objects.equals(HttpStatus.OK, statusCode)
                    ? ApiLogMessage.SUCCEED_AUTHENTICATION.getMessage()
                    : ApiLogMessage.BAD_CREDENTIAL.getMessage();
            log.info(logMessage, statusCode);
        }

        return chain.filter(exchange);
    }

}
