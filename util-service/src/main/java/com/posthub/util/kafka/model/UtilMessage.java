package com.posthub.util.kafka.model;

import com.posthub.util.model.enums.ActionType;
import com.posthub.util.model.enums.PostHubService;
import com.posthub.util.model.enums.PriorityType;
import lombok.Data;

@Data
public class UtilMessage {
    private Integer userId;
    private ActionType actionType;
    private PriorityType priorityType;
    private PostHubService service;
    private String message;
}
