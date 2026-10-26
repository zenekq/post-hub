package com.posthub.iam.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "end.points")
public record EndpointsProperties(
        String posts,
        String id,
        String create,
        String all,
        String search,
        String users,
        String auth,
        String login,
        String refreshToken,
        String register,
        String comments
) {}

