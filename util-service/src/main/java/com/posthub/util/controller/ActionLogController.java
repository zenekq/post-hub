package com.posthub.util.controller;

import com.posthub.util.model.constant.ApiLogMessage;
import com.posthub.util.model.dto.ActionLogDTO;
import com.posthub.util.model.response.UtilsResponse;
import com.posthub.util.service.ActionLogService;
import com.posthub.util.service.impl.ActionLogServiceImpl;
import com.posthub.util.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${end.points.actionsLog}")
@RequiredArgsConstructor
public class ActionLogController {

    private final ActionLogServiceImpl service;

    @GetMapping("${end.points.id}")
    public ResponseEntity<UtilsResponse<ActionLogDTO>> getActionLogById(
            @PathVariable("id") Integer logId,
            @RequestParam(name = "userId", required = false) Long userId) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        return ResponseEntity.ok(service.getActionLogById(logId, userId));
    }

}
