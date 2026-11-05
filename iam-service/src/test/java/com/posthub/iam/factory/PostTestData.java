package com.posthub.iam.factory;

import com.posthub.iam.model.dto.post.PostDTO;
import com.posthub.iam.model.entity.Post;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.request.post.NewPostRequest;
import com.posthub.iam.model.request.post.UpdateCommentRestrictionRequest;
import com.posthub.iam.model.request.post.UpdatePostRequest;

import static com.posthub.iam.factory.TestDataConstants.*;

public final class PostTestData {

    private static final UserTestData userTestData = new UserTestData();

    public static Post createTestPost(User user) {
        Post post = new Post();
        post.setId(1L);
        post.setTitle(TEST_POST_TITLE);
        post.setContent(TEST_POST_CONTENT);
        post.setCommentRestriction(TEST_COMMENT_RESTRICTION);
        post.setUser(user);

        return post;
    }

    public static Post createTestPost() {
        UserTestData.createTestUser();
        return createTestPost(userTestData.createTestUser());
    }

    public static PostDTO createTestPostDTO(Post post) {
        return new PostDTO(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getLikes(),
            post.getCreated(),
            post.getCreatedBy(),
            post.getCommentRestriction()
        );
    }

    public static PostDTO createTestPostDTO() {
        return createTestPostDTO(createTestPost());
    }

    public static NewPostRequest createTestNewPostRequest() {
        return new NewPostRequest(
                TEST_NEW_POST_TITLE,
                TEST_NEW_POST_CONTENT,
            0,
                TEST_COMMENT_RESTRICTION
        );
    }

    public static UpdatePostRequest createTestUpdatePostRequest() {
        return new UpdatePostRequest(
                TEST_UPDATE_POST_TITLE,
                TEST_UPDATE_POST_CONTENT,
                0,
                TEST_COMMENT_RESTRICTION
        );
    }

    public static UpdateCommentRestrictionRequest createTestUpdateCommentRestrictionRequest() {
        return new UpdateCommentRestrictionRequest(TEST_UPDATE_COMMENT_RESTRICTION);
    }

}
