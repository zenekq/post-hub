package com.posthub.iam.controller;

import com.posthub.iam.model.constants.ApiLogMessage;
import com.posthub.iam.model.dto.comment.CommentDTO;
import com.posthub.iam.model.request.comment.CommentRequest;
import com.posthub.iam.model.request.comment.UpdateCommentRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.service.CommentService;
import com.posthub.iam.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${end.point.comments}")
@NullMarked
public class CommentController {

    private final CommentService commentService;

    @GetMapping("${end.point.id}")
    public ResponseEntity<ApiResult<CommentDTO>> getCommentById(
            @PathVariable(name = "id") Integer commentId) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        ApiResult<CommentDTO> response = commentService.getCommentById(commentId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.point.create}")
    public ResponseEntity<ApiResult<CommentDTO>> createComment(
            @RequestBody @Valid CommentRequest commentRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        ApiResult<CommentDTO> response = commentService.createComment(commentRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("${end.point.id}")
    public ResponseEntity<ApiResult<CommentDTO>> updateComment(
            @PathVariable(name = "id") Integer commentId,
            @RequestBody @Valid UpdateCommentRequest updateCommentRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        ApiResult<CommentDTO> response = commentService.updateComment(commentId, updateCommentRequest);
        return ResponseEntity.ok(response);
    }
}
