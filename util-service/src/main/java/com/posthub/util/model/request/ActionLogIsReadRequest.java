package com.posthub.util.model.request;

import java.util.List;

public record ActionLogIsReadRequest(
    Long userId,
    List<Integer> ids
) {}
