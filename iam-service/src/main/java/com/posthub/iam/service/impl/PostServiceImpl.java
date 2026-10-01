package com.posthub.iam.service.impl;

import com.posthub.iam.mapper.PostMapper;
import com.posthub.iam.model.constants.ApiErrorMassage;
import com.posthub.iam.model.entity.Post;
import com.posthub.iam.model.exception.NotFoundException;
import com.posthub.iam.model.dto.post.PostDTO;
import com.posthub.iam.model.request.post.PostRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.repository.PostRepository;
import com.posthub.iam.service.PostService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public ApiResult<PostDTO> getById(@NotNull Integer postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new NotFoundException(ApiErrorMassage.POST_NOT_FOUND_BY_ID.format(postId)));

        PostDTO postDTO = postMapper.toPostDTO(post);

        return ApiResult.createSuccessful(postDTO);
    }

    @Override
    public ApiResult<PostDTO> createPost(@NotNull PostRequest postRequest) {

        Post post = postMapper.createPost(postRequest);
        Post savedPost = postRepository.save(post);
        PostDTO postDTO = postMapper.toPostDTO(savedPost);

        return ApiResult.createSuccessful(postDTO);
    }

}
