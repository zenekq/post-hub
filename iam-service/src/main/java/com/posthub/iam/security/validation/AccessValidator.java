package com.posthub.iam.security.validation;

import com.posthub.iam.model.constants.ApiErrorMessage;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.exception.DataExistException;
import com.posthub.iam.model.exception.InvalidDataException;
import com.posthub.iam.model.exception.NotFoundException;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.service.model.IamServiceUserRole;
import com.posthub.iam.utils.ApiUtils;
import com.posthub.iam.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component
@RequiredArgsConstructor
public class AccessValidator {

    private final UserRepository userRepository;
    private final ApiUtils apiUtils;

    public void validateNewUser(String username, String email, String password, String confirmPassword) {

        userRepository.findByUsername(username)
                .ifPresent(existingUser -> {
                    throw new DataExistException(ApiErrorMessage.USERNAME_ALREADY_EXIST.format(username));
                });

        userRepository.findByEmail(email)
                .ifPresent(existingUser -> {
                    throw new DataExistException(ApiErrorMessage.EMAIL_ALREADY_EXIST.format(email));
                });

        if (!password.equals(confirmPassword)) {
            throw new InvalidDataException(ApiErrorMessage.MISMATCH_PASSWORDS.getValue());
        }

        if (PasswordUtils.isNotValidPassword(password)) {
            throw new InvalidDataException(ApiErrorMessage.INVALID_PASSWORD.getValue());
        }

    }

    public boolean isAdminOrSuperAdmin(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(ApiErrorMessage.USER_WITH_ID_NOT_FOUND.format(userId)));

        return user.getRoles().stream()
                .map(role -> IamServiceUserRole.fromName(role.getName()))
                .anyMatch(role -> role == IamServiceUserRole.SUPER_ADMIN || role == IamServiceUserRole.ADMIN);
    }

    @SneakyThrows
   public void validateAdminOrOwnerAccess(Integer ownerUserId) {
       Integer currentUserId = apiUtils.getUserIdFromAuthentication();

       if (!currentUserId.equals(ownerUserId) && !isAdminOrSuperAdmin(currentUserId)) {
            throw new AccessDeniedException(ApiErrorMessage.HAVE_NO_ACCESS.getValue());
       }
   }

}
