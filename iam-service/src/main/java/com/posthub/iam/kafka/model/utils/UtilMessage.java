package com.posthub.iam.kafka.model.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UtilMessage {
    private Long userId;
    private ActionType actionType;
    private PriorityType priorityType;
    private PostHubService service;
    private String message;
}
