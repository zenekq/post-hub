package com.posthub.iam.security.validation;

import com.posthub.iam.model.constants.ApiErrorMessage;
import com.posthub.iam.model.entity.Post;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.enums.CommentRestriction;
import com.posthub.iam.model.exception.DataExistException;
import com.posthub.iam.model.exception.InvalidDataException;
import com.posthub.iam.model.exception.NotFoundException;
import com.posthub.iam.model.request.user.RegistrationUserRequest;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.service.model.IamServiceUserRole;
import com.posthub.iam.utils.ApiUtils;
import com.posthub.iam.security.password.PasswordValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AccessValidator {

    private final UserRepository userRepository;
    private final ApiUtils apiUtils;

    public void validateNewUser(RegistrationUserRequest request) {

        userRepository.findByUsername(request.username())
                .ifPresent(existingUser -> {
                    throw new DataExistException(ApiErrorMessage.USERNAME_ALREADY_EXIST.format(request.username()));
                });

        userRepository.findByEmail(request.email())
                .ifPresent(existingUser -> {
                    throw new DataExistException(ApiErrorMessage.EMAIL_ALREADY_EXIST.format(request.email()));
                });

        if (!request.password().equals(request.confirmPassword())) {
            throw new InvalidDataException(ApiErrorMessage.MISMATCH_PASSWORDS.getMessage());
        }

        if (PasswordValidator.isNotValidPassword(request.password())) {
            throw new InvalidDataException(ApiErrorMessage.INVALID_PASSWORD.getMessage());
        }

    }

    public boolean isAdminOrSuperAdmin(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(ApiErrorMessage.USER_WITH_ID_NOT_FOUND.format(userId)));

        return user.getRoles().stream()
                .map(role -> IamServiceUserRole.fromName(role.getName()))
                .anyMatch(role -> role == IamServiceUserRole.SUPER_ADMIN || role == IamServiceUserRole.ADMIN);
    }

    @SneakyThrows
   public void validateAdminOrOwnerAccess(Long ownerUserId) {
       Long currentUserId = apiUtils.getUserIdFromAuthentication();

       if (!currentUserId.equals(ownerUserId) && !isAdminOrSuperAdmin(currentUserId)) {
            throw new AccessDeniedException(ApiErrorMessage.HAVE_NO_ACCESS.getMessage());
       }
   }

    /**
     * Checks that the current user is allowed to write a comment under the post
     * according to the comment restriction chosen by the post owner.
     * Admins and super admins are always allowed to comment.
     */
    @SneakyThrows
    public void validateCommentPermission(Post post) {
        CommentRestriction restriction = post.getCommentRestriction();

        if (Objects.isNull(restriction) || restriction == CommentRestriction.ALL) {
            return;
        }

        Long currentUserId = apiUtils.getUserIdFromAuthentication();

        if (isAdminOrSuperAdmin(currentUserId)) {
            return;
        }

        if (restriction == CommentRestriction.ONLY_OWNER && currentUserId.equals(post.getUser().getId())) {
            return;
        }

        if (restriction == CommentRestriction.DISABLED) {
            throw new AccessDeniedException(ApiErrorMessage.COMMENTING_DISABLED_FOR_POST.format(post.getId()));
        }

        throw new AccessDeniedException(ApiErrorMessage.COMMENTING_RESTRICTED_TO_OWNER.format(post.getId()));
    }

}
