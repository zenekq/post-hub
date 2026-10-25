package com.posthub.iam.factory;

import com.posthub.iam.model.entity.Comment;
import com.posthub.iam.model.entity.Post;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.request.comment.CommentRequest;
import com.posthub.iam.model.request.comment.UpdateCommentRequest;

import java.time.LocalDateTime;

import static com.posthub.iam.factory.TestDataConstants.*;

public final class CommentTestData {

    public static Comment createTestComment(User user, Post post) {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setPost(post);
        comment.setUser(user);
        comment.setMessage(TEST_COMMENT);
        comment.setCreated(LocalDateTime.now());
        comment.setUpdated(LocalDateTime.now());
        comment.setDeleted(false);
        comment.setCreatedBy(user.getUsername());

        return comment;
    }

    public static CommentRequest createTestCommentRequest() {
        return new CommentRequest(TEST_POST_ID, TEST_COMMENT);
    }

    public static UpdateCommentRequest createTestUpdateCommentRequest() {
        return new UpdateCommentRequest(TEST_COMMENT_ID, TEST_UPDATE_COMMENT);
    }

}
