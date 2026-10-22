package com.posthub.util.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "end.points")
public record EndpointsProperties(
        String actionLogs,
        String id,
        String all,
        String markAsRead
) {}