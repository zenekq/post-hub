package com.posthub.iam.mapper;

import com.posthub.iam.model.dto.post.PostDTO;
import com.posthub.iam.model.dto.post.PostSearchDTO;
import com.posthub.iam.model.entity.Post;
import com.posthub.iam.model.request.post.NewPostRequest;
import com.posthub.iam.model.request.post.UpdatePostRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PostMapper {

    @Mapping(source = "created", target = "created", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    PostDTO toPostDTO(Post post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    Post createPost(NewPostRequest newPostRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    void updatePost(@MappingTarget Post post, UpdatePostRequest updatePostRequest);

    @Mapping(target = "createdBy", source = "user.username")
    PostSearchDTO toPostSearchDTO(Post post);

}
