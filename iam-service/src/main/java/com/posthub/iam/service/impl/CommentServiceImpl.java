package com.posthub.iam.service.impl;

import com.posthub.iam.mapper.CommentMapper;
import com.posthub.iam.model.dto.comment.CommentDTO;
import com.posthub.iam.model.entity.Comment;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.repository.CommentRepository;
import com.posthub.iam.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional(readOnly = true)
    public ApiResult<CommentDTO> getCommentById(Integer commentId) {

        Comment comment = commentRepository.findByIdAndDeletedFalseOrThrow(commentId);
        CommentDTO commentDTO = commentMapper.toDTO(comment);

        return ApiResult.createSuccessful(commentDTO);
    }

}
