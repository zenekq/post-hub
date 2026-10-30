package com.gateway.service;

import com.gateway.utils.StackInfo;
import tools.jackson.databind.ObjectMapper;
import com.gateway.config.PublicApiPaths;
import com.gateway.model.*;
import com.gateway.model.constants.ApiConstants;
import com.gateway.model.constants.ApiLogMessage;
import com.gateway.model.constants.ApiMessage;
import com.gateway.model.constants.MicroServiceNames;
import com.gateway.service.feign.RemoteIamService;
import com.gateway.utils.ApiUtils;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.utils.Constants;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;

@Slf4j
@Service
public class RequestAuthorizationService {
    private final MicroServiceNames microServiceNames;
    private final ObjectMapper objectMapper;
    private final RemoteIamService remoteIamService;
    private final PublicApiPaths publicApiPaths;

    public RequestAuthorizationService(
            MicroServiceNames microServiceNames, ObjectMapper objectMapper, @Lazy RemoteIamService remoteIamService, PublicApiPaths publicApiPaths
    ) {
        this.microServiceNames = microServiceNames;
        this.remoteIamService = remoteIamService;
        this.objectMapper = objectMapper;
        this.publicApiPaths = publicApiPaths;
    }

    public Mono<Void> validateKeyHeader(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), StackInfo.getMethodName());

        ServerHttpRequest request = exchange.getRequest();
        List<String> authHeaders = getHeaders(request, ApiConstants.ACCESS_KEY_HEADER_NAME);

        if (authHeaders.isEmpty()) {
            log.info(ApiLogMessage.KEY_NOT_VALID.getMessage());
            return getUnauthorizedResponse(exchange, ApiLogMessage.KEY_NOT_VALID.getMessage());
        }

        log.info(ApiLogMessage.KEY_VALID.getMessage());
        return chain.filter(exchange);
    }

    public Mono<Void> validateTokenHeader(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), StackInfo.getMethodName());

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        List<String> publicPaths = publicApiPaths.getPublicPaths();
        boolean isPublicPath = publicPaths.stream().anyMatch(path::startsWith);

        if (isPublicPath) {
            return chain.filter(exchange);
        }

        Optional<PostHubJwt> jwtOptional = getTokens(request);
        if (jwtOptional.isEmpty() || Objects.isNull(jwtOptional.get().getJwt()) || jwtOptional.get().getJwt().isEmpty()) {
            return getUnauthorizedResponse(exchange, ApiLogMessage.TOKEN_IS_INVALID.getMessage());
        }

        return chain.filter(exchange);
    }

    public Mono<Void> validateAccessToService(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), StackInfo.getMethodName());

        String requiredBodyField;
        URI uri = exchange.getRequest().getURI();
        ServerHttpRequest request = exchange.getRequest();
        String endPoint = request.getPath().toString();
        HttpMethod requestHttpMethod = request.getMethod();
        log.info(ApiLogMessage.URL_IN_AUTH.getMessage(),
                requestHttpMethod.name(),
                uri
        );
        if (Objects.isNull(endPoint)) {
            return getUnauthorizedResponse(exchange, ApiMessage.ACCESS_NOT_GRANTED.getMessage());
        }

        requiredBodyField = handleRequiredFieldsForAccessValidation(exchange, request, requestHttpMethod);
        log.warn("Body: '{}'", requiredBodyField);

        Optional<PostHubJwt> jwtOptional = getTokens(request);

        if (jwtOptional.isEmpty()) {
            return getUnauthorizedResponse(exchange, ApiMessage.ACCESS_NOT_GRANTED.getMessage());
        }
        PostHubJwt postHubJwt = jwtOptional.get();
        String jwt = postHubJwt.getJwt();

        try {
            GateWayResponse<String> gateWayResponse = authorizeAccessToService(
                    requestHttpMethod.name(), endPoint, jwt, requiredBodyField
            );

            if (gateWayResponse.isFailed()) {
                log.info(
                        ApiLogMessage.SERVICE_RESPONSE_IS_FAILED.getMessage(),
                        microServiceNames.getIamServiceName(),
                        gateWayResponse.getMessage()
                );
                ServerHttpResponse response = exchange.getResponse();
                if (Objects.equals(JwtType.COOKIE, postHubJwt.getType())) {
                    response.addCookie(ApiUtils.blockAuthCookie());
                }
                return getUnauthorizedResponse(exchange, gateWayResponse.getMessage());
            }

            log.info(ApiLogMessage.TOKEN_IS_VALID.getMessage());
            ServerWebExchange webExchange = ApiUtils
                    .addHeader(exchange, HttpHeaders.AUTHORIZATION, gateWayResponse.getPayload());
            return chain.filter(webExchange);
        } catch (FeignException cause) {
            log.warn(ApiLogMessage.AUTH_FILTER_ERROR.getMessage(), cause.getMessage());

            int feignStatus = cause.status();
            HttpStatus httpStatus = HttpStatus.resolve(feignStatus);
            if (feignStatus < 0 || Objects.isNull(httpStatus)) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
                return response.setComplete();
            }
            if (isUnauthorizedResponse(feignStatus, cause.getMessage()) ) {
                return getUnauthorizedResponse(exchange, cause.getMessage());
            }
            log.info("Feign status: {}", Optional.of(feignStatus));
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(httpStatus);
            return response.setComplete();
        }
    }

    private boolean isUnauthorizedResponse(int feignStatus, String message) {
        return (feignStatus > 401 && feignStatus < 500) || message.contains("AuthenticationCredentialsNotFoundException: An Authentication object was no");
    }

    private Optional<PostHubJwt> getTokens(ServerHttpRequest request) {
        log.info(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), StackInfo.getMethodName());

        List<String> authHeaders = getHeaders(request, HttpHeaders.AUTHORIZATION);
        log.info("AuthHeaders: {}", Arrays.toString(authHeaders.toArray()));
        for (String headerValue : authHeaders) {
            if (Objects.nonNull(headerValue) && isJwtValid(headerValue)) {
                return Optional.of(new PostHubJwt(JwtType.HEADER, headerValue));
            }
        }

        if (!authHeaders.isEmpty()) {
            log.warn(ApiLogMessage.INVALID_AUTHORIZE_HEADER.getMessage() + " line 164");
            return Optional.empty();
        }

        MultiValueMap<String, HttpCookie> requestCookies = request.getCookies();
        for (Map.Entry<String, List<HttpCookie>> entry : requestCookies.entrySet()) {
            log.warn("Cookie {}: List<HttpCookie>: '{}'",
                    entry.getKey(),
                    Arrays.toString(entry.getValue().toArray())
            );
        }
        HttpCookie requestAuthCookie = requestCookies.getFirst(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(requestAuthCookie) || requestAuthCookie.getValue().isEmpty()) {
            return Optional.empty();
        }

        String jwt = requestAuthCookie.getValue();
        return Optional.of(new PostHubJwt(JwtType.COOKIE, jwt));
    }

    private String handleRequiredFieldsForAccessValidation(
            ServerWebExchange exchange, ServerHttpRequest request, HttpMethod requestHttpMethod
    ) {
        log.info(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), StackInfo.getMethodName());

        GateWayRequiredBodyField requiredBodyField = new GateWayRequiredBodyField();
        if (Objects.equals(HttpMethod.GET, requestHttpMethod) || Objects.equals(HttpMethod.DELETE, requestHttpMethod)) {
            MultiValueMap<String, String> queryParams = request.getQueryParams();
            String userId = queryParams.getFirst("userId");
            if (StringUtils.isNumeric(userId)) {
                requiredBodyField.setUserId(Integer.parseInt(userId));
            }
        } else {
            requiredBodyField = parseBodyFromRequest(exchange);
        }

        return objectMapper.writeValueAsString(requiredBodyField);
    }

    private GateWayResponse<String> authorizeAccessToService(
            String httpMethodName, String endPoint, String jwt, String requiredBodyField
    ) {
        log.info(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), StackInfo.getMethodName());

        GateWayRequest<String> gateWayRequest = GateWayRequest.create(endPoint, httpMethodName, requiredBodyField);

        return remoteIamService.requestAccess(jwt, gateWayRequest);
    }

    private List<String> getHeaders(ServerHttpRequest request, String httpHeader) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), StackInfo.getMethodName());

        if (request.getHeaders().getFirst(httpHeader) == null) {

            log.info(ApiLogMessage.INVALID_AUTHORIZE_HEADER.getMessage() + " line 219");
            return Collections.emptyList();
        }
        List<String> authHeaders = request.getHeaders().get(httpHeader);
        if (Objects.isNull(authHeaders) || authHeaders.isEmpty()) {
            log.info(ApiLogMessage.EMPTY_AUTHORIZE_HEADERS.getMessage());
            return Collections.emptyList();
        }
        return authHeaders;
    }

    private GateWayRequiredBodyField parseBodyFromRequest(ServerWebExchange exchange) {
        log.debug(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), StackInfo.getMethodName());

        LinkedHashMap<String,Object> requestBody;
        GateWayRequiredBodyField requiredBodyField = new GateWayRequiredBodyField();
        try {
            requestBody = exchange.getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);
            log.warn("Request's body: '{}'", Objects.nonNull(requestBody) ? requestBody.toString() : null);
            Integer userId = Objects.nonNull(requestBody) ? (Integer) requestBody.get("userId") : null;
            requiredBodyField.setUserId(userId);
        } catch (Exception e) {
            log.info("Exception during parsing userId: ", e);
        }
        return requiredBodyField;
    }

    private boolean isJwtValid(String token) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), StackInfo.getMethodName());

        return Objects.nonNull(token) && token.startsWith(ApiConstants.TOKEN_PREFIX) && token.contains(Constants.DOT);
    }

    private Mono<Void> getUnauthorizedResponse(ServerWebExchange exchange, String error) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), StackInfo.getMethodName());

        log.info(ApiLogMessage.AUTH_FILTER_ERROR.getMessage(), error);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}
