package com.posthub.util.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ActionLogUpdateResultDTO {
    private int updatedCount;
    private List<Integer> updatedIds;
    private List<Integer> skippedIds;
}
