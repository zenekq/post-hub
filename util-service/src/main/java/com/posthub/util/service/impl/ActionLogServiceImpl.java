package com.posthub.util.service.impl;

import com.posthub.util.entity.ActionLog;
import com.posthub.util.kafka.model.UtilMessage;
import com.posthub.util.mapper.ActionLogMapper;
import com.posthub.util.model.constant.ApiErrorMessage;
import com.posthub.util.model.dto.ActionLogDTO;
import com.posthub.util.model.exception.NotFoundException;
import com.posthub.util.model.request.ActionLogIsReadRequest;
import com.posthub.util.model.request.ActionLogUpdateResultDTO;
import com.posthub.util.model.response.PaginationResponse;
import com.posthub.util.model.response.UtilsResponse;
import com.posthub.util.repository.ActionLogRepository;
import com.posthub.util.service.ActionLogService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public UtilsResponse<PaginationResponse<ActionLogDTO>> findAllActionLogs(Pageable pageable) {

        Page<ActionLogDTO> logs = repository.findAll(pageable).map(mapper::actionLogToActionLogDTO);

        PaginationResponse<ActionLogDTO> paginationResponse = new PaginationResponse<>(
                logs.getContent(),
                new PaginationResponse.Pagination(
                        logs.getTotalElements(),
                        pageable.getPageSize(),
                        logs.getNumber() + 1,
                        logs.getTotalPages()
                )
        );

        return UtilsResponse.createSuccessful(paginationResponse);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public UtilsResponse<ActionLogUpdateResultDTO> setIsReadEqualsTrue(@NotNull ActionLogIsReadRequest request) {

        Long currentUserId = request.userId();

        List<ActionLog> logs = repository.findAllById(request.ids());

        Map<Boolean, List<Integer>> partitioned = logs.stream()
                .collect(Collectors.partitioningBy(
                        log -> log.getUserId().equals(currentUserId),
                        Collectors.mapping(ActionLog::getId, Collectors.toList())
                ));

        List<Integer> allowedIds = partitioned.get(true);
        List<Integer> skippedIds = partitioned.get(false);

        int updatedCount = allowedIds.isEmpty() ? 0 : repository.setIsReadEqualsTrue(allowedIds);

        return UtilsResponse.createSuccessful(
                ActionLogUpdateResultDTO.builder()
                        .updatedCount(updatedCount)
                        .updatedIds(allowedIds)
                        .skippedIds(skippedIds)
                        .build()
        );
    }

    @Override
    @Transactional
    public ActionLog saveLogFromKafkaMessage(UtilMessage message) {
        ActionLog actionLog = mapper.mapKafkaMessageToEntity(message);
        return repository.save(actionLog);
    }
}
