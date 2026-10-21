package com.posthub.util.model.dto;

import com.posthub.util.model.enums.ActionType;
import com.posthub.util.model.enums.PostHubService;
import com.posthub.util.model.enums.PriorityType;

import java.time.LocalDateTime;

public record ActionLogDTO(
    Integer id,
    Long userId,

    ActionType actionType,
    PriorityType priorityType,
    PostHubService service,

    LocalDateTime createdAt,
    String message,
    Boolean isRead
) {}
