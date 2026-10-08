package com.posthub.iam.service.impl;

import com.posthub.iam.mapper.PostMapper;
import com.posthub.iam.model.constants.ApiErrorMessage;
import com.posthub.iam.model.dto.post.PostSearchDTO;
import com.posthub.iam.model.entity.Post;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.exception.DataExistException;
import com.posthub.iam.model.exception.NotFoundException;
import com.posthub.iam.model.dto.post.PostDTO;
import com.posthub.iam.model.request.post.NewPostRequest;
import com.posthub.iam.model.request.post.PostSearchRequest;
import com.posthub.iam.model.request.post.UpdatePostRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.model.responce.PaginationResponse;
import com.posthub.iam.repository.PostRepository;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.repository.criteria.PostSearchCriteria;
import com.posthub.iam.security.validation.AccessValidator;
import com.posthub.iam.service.PostService;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;
    private final AccessValidator accessValidator;

    @Override
    public ApiResult<PostDTO> getById(@NotNull Integer postId) {

        Post post = postRepository.findByIdAndDeletedFalse(postId)
                .orElseThrow(() ->
                        new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.format(postId)));

        PostDTO postDTO = postMapper.toPostDTO(post);

        return ApiResult.createSuccessful(postDTO);
    }

    @Override
    public ApiResult<PostDTO> createPost(@NotNull NewPostRequest newPostRequest, String username) {

        if (postRepository.existsByTitle(newPostRequest.getTitle())) {
            throw new DataExistException(ApiErrorMessage.POST_ALREADY_EXIST.format(newPostRequest.getTitle()));
        }

        User user = userRepository.findByUsernameOrThrow(username);

        Post post = postMapper.createPost(newPostRequest);
        post.setUser(user);
        post.setCreatedBy(username);
        Post savedPost = postRepository.save(post);
        PostDTO postDTO = postMapper.toPostDTO(savedPost);

        return ApiResult.createSuccessful(postDTO);
    }

    @Override
    public ApiResult<PostDTO> updatePost(@NotNull Integer postId, @NotNull UpdatePostRequest updatePostRequest) {

        Post post = postRepository.findByIdAndDeletedFalse(postId)
                .orElseThrow(() ->
                        new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.format(postId)));

        accessValidator.validateAdminOrOwnerAccess(post.getUser().getId());

        if (postRepository.existsByTitle(updatePostRequest.getTitle())) {
            throw new DataExistException(ApiErrorMessage.POST_ALREADY_EXIST.format(updatePostRequest.getTitle()));
        }

        postMapper.updatePost(post, updatePostRequest);
        post.setUpdated(LocalDateTime.now());
        Post savedPost = postRepository.save(post);

        PostDTO postDTO = postMapper.toPostDTO(savedPost);

        return ApiResult.createSuccessful(postDTO);
    }

    @Override
    public void softDeletePost(Integer postId) {

        Post post = postRepository.findByIdAndDeletedFalse(postId)
                .orElseThrow(() ->
                        new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.format(postId)));

        accessValidator.validateAdminOrOwnerAccess(post.getUser().getId());

        post.setDeleted(true);
        postRepository.save(post);
    }

    @Override
    public ApiResult<PaginationResponse<PostSearchDTO>> findAllPosts(Pageable pageable) {

        Page<PostSearchDTO> posts = postRepository.findAll(pageable)
                .map(postMapper::toPostSearchDTO);

        PaginationResponse<PostSearchDTO> paginationResponse = new PaginationResponse<>(
                posts.getContent(),
                new PaginationResponse.Pagination(
                        posts.getTotalElements(),
                        pageable.getPageSize(),
                        posts.getNumber() + 1,
                        posts.getTotalPages()
                )
        );

        return ApiResult.createSuccessful(paginationResponse);
    }

    @Override
    public ApiResult<PaginationResponse<PostSearchDTO>> searchPosts(
            @NonNull PostSearchRequest postSearchRequest, Pageable pageable) {

        Specification<Post> specification = new PostSearchCriteria(postSearchRequest);

        Page<PostSearchDTO> posts = postRepository.findAll(specification, pageable)
                .map(postMapper::toPostSearchDTO);

        PaginationResponse<PostSearchDTO> paginationResponse = PaginationResponse.<PostSearchDTO>builder()
                .content(posts.getContent())
                .pagination(PaginationResponse.Pagination.builder()
                        .total(posts.getTotalElements())
                        .limit(pageable.getPageSize())
                        .page(posts.getNumber() + 1)
                        .pages(posts.getTotalPages())
                        .build()
                ).build();

        return ApiResult.createSuccessful(paginationResponse);
    }

}
