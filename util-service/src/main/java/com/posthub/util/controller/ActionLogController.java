package com.posthub.util.controller;

import com.posthub.util.model.constant.ApiLogMessage;
import com.posthub.util.model.dto.ActionLogDTO;
import com.posthub.util.model.request.ActionLogIsReadRequest;
import com.posthub.util.model.request.ActionLogUpdateResultDTO;
import com.posthub.util.model.request.PaginationRequest;
import com.posthub.util.model.response.PaginationResponse;
import com.posthub.util.model.response.UtilsResponse;
import com.posthub.util.service.impl.ActionLogServiceImpl;
import com.posthub.util.utils.StackInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${end.points.actionLogs}")
@RequiredArgsConstructor
public class ActionLogController {

    private final ActionLogServiceImpl service;

    @GetMapping("${end.points.id}")
    public ResponseEntity<UtilsResponse<ActionLogDTO>> getActionLogById(
            @PathVariable("id") Integer logId,
            @RequestParam(name = "userId", required = false) Long userId) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), StackInfo.getMethodName());

        return ResponseEntity.ok(service.getActionLogById(logId, userId));
    }

    @GetMapping("${end.points.all}")
    public ResponseEntity<UtilsResponse<PaginationResponse<ActionLogDTO>>> getAllActionLogs(
            @ModelAttribute PaginationRequest paginationRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), StackInfo.getMethodName());

        Pageable pageRequest = PageRequest.of(paginationRequest.page(), paginationRequest.limit());

        UtilsResponse<PaginationResponse<ActionLogDTO>> response = service.findAllActionLogs(pageRequest);

        return ResponseEntity.ok(response);
    }

    @PutMapping("${end.points.markAsRead}")
    public ResponseEntity<UtilsResponse<ActionLogUpdateResultDTO>> setIsReadEqualsTrue(
            @RequestBody @Valid ActionLogIsReadRequest request) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), StackInfo.getMethodName());

        return ResponseEntity.ok(service.setIsReadEqualsTrue(request));
    }


}
