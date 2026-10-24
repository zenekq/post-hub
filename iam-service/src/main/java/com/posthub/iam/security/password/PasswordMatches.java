package com.posthub.iam.security.password;

import com.posthub.iam.model.request.user.RegistrationUserRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatches.PasswordMatchesValidator.class)
public @interface PasswordMatches {

    String message() default "Passwords do not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegistrationUserRequest> {
        @Override
        public boolean isValid(RegistrationUserRequest request, ConstraintValidatorContext context) {
            return request.password().equals(request.confirmPassword());
        }
    }
}
