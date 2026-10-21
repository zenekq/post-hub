package com.posthub.util.service;

import com.posthub.util.model.dto.ActionLogDTO;
import com.posthub.util.model.response.UtilsResponse;

public interface ActionLogService {

    UtilsResponse<ActionLogDTO> getActionLogById(Integer logId, Long userId);

}
