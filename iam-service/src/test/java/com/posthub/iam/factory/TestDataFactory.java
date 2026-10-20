package com.posthub.iam.factory;

import com.posthub.iam.model.dto.post.PostDTO;
import com.posthub.iam.model.dto.role.RoleDTO;
import com.posthub.iam.model.dto.user.UserDTO;
import com.posthub.iam.model.entity.Comment;
import com.posthub.iam.model.entity.Post;
import com.posthub.iam.model.entity.Role;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.request.post.NewPostRequest;
import com.posthub.iam.model.request.user.NewUserRequest;
import com.posthub.iam.model.request.user.UpdateUserRequest;
import com.posthub.iam.service.model.IamServiceUserRole;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class TestDataFactory {

    public static final Long TEST_USER_ID = 1L;
    public static final String TEST_USERNAME = "TestUser";
    public static final String TEST_USERNAME_NEW_USER = "TestNewUser";
    public static final String TEST_UPDATE_USERNAME = "TestNewUser";
    public static final String TEST_EMAIL = "test_user@example.com";
    public static final String TEST_NEW_USER_EMAIL = "test_new_user@example.com";
    public static final String TEST_UPDATE_EMAIL = "test_update_user@example.com";
    public static final String TEST_POST_TITLE = "Test Post";
    public static final String TEST_POST_CONTENT = "Test Content";
    public static final String TEST_COMMENT = "Test Comment";
    public static final String TEST_PASSWORD = "password1A!";
    public static final String TEST_ENCODED_PASSWORD = "encodedPassword";

    public User createTestUser() {

        User user = new User();

        user.setId(TEST_USER_ID);
        user.setUsername(TEST_USERNAME);
        user.setEmail(TEST_EMAIL);
        user.setPassword(TEST_ENCODED_PASSWORD);

        Role role = new Role();
        role.setName(IamServiceUserRole.USER.getRole());
        user.setRoles(List.of(role));

        return user;
    }

    public UserDTO createTestUserDTO() {
        return createTestUserDTO(new User());
    }

    public UserDTO createTestUserDTO(User user) {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreated(user.getCreated());
        userDTO.setLastLogin(user.getLastLogin());
        userDTO.setRegistrationStatus(user.getRegistrationStatus());
        userDTO.setRoles(mapRoles(user.getRoles()));

        return userDTO;
    }

    public NewUserRequest createTestNewUserRequest() {
        return new NewUserRequest(
                TEST_USERNAME_NEW_USER,
                TEST_PASSWORD,
                TEST_NEW_USER_EMAIL);
    }

    public UpdateUserRequest createTestUpdateUserRequest() {
        return new UpdateUserRequest(
                TEST_UPDATE_USERNAME,
                TEST_NEW_USER_EMAIL,
                false);
    }


    public List<RoleDTO> mapRoles(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new RoleDTO(role.getId(), role.getName()))
                .toList();
    }

    public Post createTestPost() {
        return createTestPost(new User());
    }

    public Post createTestPost(User user) {
        Post post = new Post();
        post.setId(1L);
        post.setTitle(TEST_POST_TITLE);
        post.setContent(TEST_POST_CONTENT);
        post.setUser(user);

        return post;
    }

    public PostDTO createTestPostDTO() {
        return createTestPostDTO(new Post());
    }

    public PostDTO createTestPostDTO(Post post) {
        return new PostDTO(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getLikes(),
            post.getCreated(),
            post.getCreatedBy()
        );
    }

    public NewPostRequest createTestNewPostRequest() {
        return new NewPostRequest(
            "New post title",
            "New post content",
            0
        );
    }

    public Comment createTestComment() {
        return createTestComment(createTestUser(), createTestPost());
    }

    public Comment createTestComment(User user, Post post) {
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
}
