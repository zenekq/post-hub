package com.posthub.iam.service.impl;

import com.posthub.iam.mapper.CommentMapper;
import com.posthub.iam.mapper.PostMapper;
import com.posthub.iam.model.dto.comment.CommentDTO;
import com.posthub.iam.model.dto.comment.CommentSearchDTO;
import com.posthub.iam.model.dto.post.PostDTO;
import com.posthub.iam.model.entity.Comment;
import com.posthub.iam.model.entity.Post;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.request.comment.CommentRequest;
import com.posthub.iam.model.request.comment.UpdateCommentRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.model.responce.PaginationResponse;
import com.posthub.iam.repository.CommentRepository;
import com.posthub.iam.repository.PostRepository;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.service.CommentService;
import com.posthub.iam.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ApiUtils apiUtils;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    @Transactional(readOnly = true)
    public ApiResult<CommentDTO> getCommentById(Integer commentId) {

        Comment comment = commentRepository.findByIdAndDeletedFalseOrThrow(commentId);
        CommentDTO commentDTO = commentMapper.toDTO(comment);

        return ApiResult.createSuccessful(commentDTO);
    }

    @Override
    @Transactional
    public ApiResult<CommentDTO> createComment(CommentRequest request) {

        Integer userId = apiUtils.getUserIdFromAuthentication();

        User user = userRepository.findByIdAndDeletedFalseOrThrow(userId);
        Post post = postRepository.findByIdAndDeletedFalseOrThrow(request.getPostId());

        Comment comment = commentMapper.createComment(request, user, post);
        Comment savedComment = commentRepository.save(comment);
        postRepository.save(post);

        CommentDTO commentDTO = commentMapper.toDTO(savedComment);

        return ApiResult.createSuccessful(commentDTO);
    }

    @Override
    @Transactional
    public ApiResult<CommentDTO> updateComment(Integer commentId, UpdateCommentRequest request) {

        Comment comment = commentRepository.findByIdAndDeletedFalseOrThrow(commentId);

        if (request.getPostId() != null) {
            Post post = postRepository.findByIdAndDeletedFalseOrThrow(request.getPostId());
            comment.setPost(post);
        }

        commentMapper.updateComment(comment, request);
        Comment savedComment = commentRepository.save(comment);
        CommentDTO commentDTO = commentMapper.toDTO(savedComment);

        return ApiResult.createSuccessful(commentDTO);
    }

    @Override
    @Transactional
    public void softDelete(Integer commentId) {

        Comment comment = commentRepository.findByIdAndDeletedFalseOrThrow(commentId);

        comment.setDeleted(true);
        commentRepository.save(comment);

        Post post = comment.getPost();
        postRepository.save(post);

        PostDTO postDTO = postMapper.toPostDTO(post);

        ApiResult.createSuccessful(postDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResult<PaginationResponse<CommentSearchDTO>> findAllComments(Pageable pageable) {

        Page<CommentSearchDTO> comments = commentRepository.findAll(pageable)
                .map(commentMapper::toCommentSearchDTO);

        PaginationResponse<CommentSearchDTO> paginationResponse = new PaginationResponse<>(
                comments.getContent(),
                new PaginationResponse.Pagination(
                        comments.getTotalElements(),
                        pageable.getPageSize(),
                        comments.getNumber() + 1,
                        comments.getTotalPages()
                )
        );

        return ApiResult.createSuccessful(paginationResponse);
    }

}
