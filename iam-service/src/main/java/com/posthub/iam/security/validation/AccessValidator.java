package com.posthub.iam.security.validation;

import com.posthub.iam.model.constants.ApiErrorMessage;
import com.posthub.iam.model.exception.DataExistException;
import com.posthub.iam.model.exception.InvalidDataException;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessValidator {

    private final UserRepository userRepository;

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

}
