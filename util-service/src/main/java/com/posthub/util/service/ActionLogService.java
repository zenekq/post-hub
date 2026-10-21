package com.posthub.util.service;

import com.posthub.util.model.dto.ActionLogDTO;
import com.posthub.util.model.response.PaginationResponse;
import com.posthub.util.model.response.UtilsResponse;
import org.springframework.data.domain.Pageable;

public interface ActionLogService {

    UtilsResponse<ActionLogDTO> getActionLogById(Integer logId, Long userId);

    UtilsResponse<PaginationResponse<ActionLogDTO>> findAllActionLogs(Pageable pageable);

}
