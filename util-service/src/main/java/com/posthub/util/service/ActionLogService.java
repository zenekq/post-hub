package com.posthub.util.service;

import com.posthub.util.entity.ActionLog;
import com.posthub.util.kafka.model.UtilMessage;
import com.posthub.util.model.dto.ActionLogDTO;
import com.posthub.util.model.request.ActionLogIsReadRequest;
import com.posthub.util.model.request.ActionLogUpdateResultDTO;
import com.posthub.util.model.response.PaginationResponse;
import com.posthub.util.model.response.UtilsResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;

public interface ActionLogService {

    UtilsResponse<ActionLogDTO> getActionLogById(Integer logId, Long userId);

    UtilsResponse<PaginationResponse<ActionLogDTO>> findAllActionLogs(Pageable pageable);

    UtilsResponse<ActionLogUpdateResultDTO> setIsReadEqualsTrue(
            @NotNull ActionLogIsReadRequest request);

    ActionLog saveLogFromKafkaMessage(UtilMessage utilMessage);

}
