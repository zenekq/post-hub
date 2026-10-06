package com.posthub.iam.security.validation;

import com.posthub.iam.model.request.user.RegistrationUserRequest;
import com.posthub.iam.utils.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegistrationUserRequest> {
    @Override
    public boolean isValid(RegistrationUserRequest request, ConstraintValidatorContext context) {
        return request.getPassword().equals(request.getConfirmPassword());
    }
}
