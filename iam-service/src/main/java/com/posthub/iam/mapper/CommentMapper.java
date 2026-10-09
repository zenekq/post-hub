package com.posthub.iam.mapper;

import com.posthub.iam.model.dto.comment.CommentDTO;
import com.posthub.iam.model.entity.Comment;
import com.posthub.iam.model.entity.Post;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.request.comment.CommentRequest;
import com.posthub.iam.model.request.comment.UpdateCommentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CommentMapper {

    @Mapping(source = "user.id", target = "owner.id")
    @Mapping(source = "user.username", target = "owner.username")
    @Mapping(source = "user.email", target = "owner.email")
    @Mapping(source = "post.id", target = "postId")
    CommentDTO toDTO(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "createdBy", source = "user.email")
    Comment createComment(CommentRequest commentRequest, User user, Post post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateComment(@MappingTarget Comment comment, UpdateCommentRequest request);

}
