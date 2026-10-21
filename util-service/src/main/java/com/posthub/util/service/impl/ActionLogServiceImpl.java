package com.posthub.util.service.impl;

import com.posthub.util.entity.ActionLog;
import com.posthub.util.mapper.ActionLogMapper;
import com.posthub.util.model.constant.ApiErrorMessage;
import com.posthub.util.model.dto.ActionLogDTO;
import com.posthub.util.model.exception.NotFoundException;
import com.posthub.util.model.response.UtilsResponse;
import com.posthub.util.repository.ActionLogRepository;
import com.posthub.util.service.ActionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActionLogServiceImpl implements ActionLogService {

    private final ActionLogRepository repository;
    private final ActionLogMapper mapper;

    @Override
    public UtilsResponse<ActionLogDTO> getActionLogById(Integer logId, Long userID) {

        ActionLog actionLog;

        if(userID == null){
            actionLog = repository.findById(logId)
                    .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_ACTION_LOG.format(logId)));
        } else {
            actionLog = repository.findByIdAndUserId(logId, userID).
                    orElseThrow(() -> new NotFoundException(
                            ApiErrorMessage.NOT_FOUND_ACTION_LOG_FOR_USER.format(logId,  userID)));
        }

        return UtilsResponse.createSuccessful(mapper.actionLogToActionLogDTO(actionLog));
    }
}
