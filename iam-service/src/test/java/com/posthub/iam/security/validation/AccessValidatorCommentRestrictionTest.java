package com.posthub.iam.security.validation;

import com.posthub.iam.model.entity.Post;
import com.posthub.iam.model.entity.Role;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.enums.CommentRestriction;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.service.model.IamServiceUserRole;
import com.posthub.iam.utils.ApiUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class AccessValidatorCommentRestrictionTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApiUtils apiUtils;

    private AccessValidator accessValidator;

    private Post testPost;

    private static final Long POST_ID = 1L;
    private static final Long OWNER_ID = 1L;
    private static final Long OTHER_USER_ID = 2L;
    private static final Long ADMIN_USER_ID = 3L;

    @BeforeEach
    void setUp() {
        accessValidator = new AccessValidator(userRepository, apiUtils);

        testPost = new Post();
        testPost.setId(POST_ID);
        testPost.setUser(createUser(OWNER_ID, IamServiceUserRole.USER));
    }

    @Test
    void validateCommentPermission_RestrictionAll_OwnerIsAllowed() {
        testPost.setCommentRestriction(CommentRestriction.ALL);

        assertDoesNotThrow(() -> accessValidator.validateCommentPermission(testPost));
    }

    @Test
    void validateCommentPermission_OnlyOwner_OwnerIsAllowed() {
        testPost.setCommentRestriction(CommentRestriction.ONLY_OWNER);

        when(apiUtils.getUserIdFromAuthentication()).thenReturn(OWNER_ID);

        assertDoesNotThrow(() -> accessValidator.validateCommentPermission(testPost));
    }

    @Test
    void validateCommentPermission_OnlyOwner_OtherUserIsForbidden() {
        testPost.setCommentRestriction(CommentRestriction.ONLY_OWNER);

        when(apiUtils.getUserIdFromAuthentication()).thenReturn(OTHER_USER_ID);
        when(userRepository.findById(OTHER_USER_ID))
                .thenReturn(Optional.of(createUser(OTHER_USER_ID, IamServiceUserRole.USER)));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> accessValidator.validateCommentPermission(testPost));

        assertTrue(exception.getMessage().contains("author"));
    }

    @Test
    void validateCommentPermission_Disabled_OwnerIsForbidden() {
        testPost.setCommentRestriction(CommentRestriction.DISABLED);

        when(apiUtils.getUserIdFromAuthentication()).thenReturn(OWNER_ID);
        when(userRepository.findById(OWNER_ID)).thenReturn(Optional.of(testPost.getUser()));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> accessValidator.validateCommentPermission(testPost));

        assertTrue(exception.getMessage().contains("disabled"));
    }

    @Test
    void validateCommentPermission_Disabled_AdminIsAllowed() {
        testPost.setCommentRestriction(CommentRestriction.DISABLED);

        when(apiUtils.getUserIdFromAuthentication()).thenReturn(ADMIN_USER_ID);
        when(userRepository.findById(ADMIN_USER_ID))
                .thenReturn(Optional.of(createUser(ADMIN_USER_ID, IamServiceUserRole.ADMIN)));

        assertDoesNotThrow(() -> accessValidator.validateCommentPermission(testPost));
    }

    @Test
    void validateCommentPermission_RestrictionIsNotSet_OtherUserIsAllowed() {
        testPost.setCommentRestriction(null);

        assertDoesNotThrow(() -> accessValidator.validateCommentPermission(testPost));
    }

    private User createUser(Long id, IamServiceUserRole role) {
        User user = new User();
        user.setId(id);
        user.setUsername("user_" + id);

        Role userRole = new Role();
        userRole.setName(role.getRole());
        user.setRoles(List.of(userRole));

        return user;
    }

}
