package com.posthub.iam.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
    POST_NOT_FOUND_BY_ID("Post with ID: {} was not found"),
    POST_ALREADY_EXIST("Post with Title: {} already exists"),
    USER_WITH_ID_NOT_FOUND("User not found with ID: {}"),
    USER_WITH_USERNAME_NOT_FOUND("User with username: {} not found: "),
    USERNAME_ALREADY_EXIST("Username: {} already exists"),
    EMAIL_ALREADY_EXIST("Email: {} already exists"),
    USER_ROLE_NOT_FOUND("Role: {} was not found"),
    EMAIL_NOT_FOUND("Email: {} not found."),
    USERNAME_NOT_FOUND("Username: {} not found."),
    INVALID_TOKEN_SIGNATURE("Invalid token signature"),
    COMMENT_NOT_FOUND_BY_ID("Comment with ID: {} was not found"),

    ERROR_DURING_JWT_PROCESSING("Error occurred while processing JWT"),
    TOKEN_EXPIRED("Token has expired"),
    UNEXPECTED_ERROR_OCCURRED("Unexpected error. Please try again later"),

    AUTHENTICATION_FAILED_FOR_USER("Authentication failed for user: {}. "),
    INVALID_USER_OR_PASSWORD("Invalid email or password. Try again"),
    INVALID_USER_REGISTRATION_STATUS("Invalid user registration status: {}. "),
    NOT_FOUND_REFRESH_TOKEN("Refresh token not found."),

    MISMATCH_PASSWORDS("Password does not match"),
    INVALID_PASSWORD("Invalid password. It must have: "
            + "length at least " + ApiConstants.REQUIRED_MIN_PASSWORD_LENGTH + ", including "
            + ApiConstants.REQUIRED_MIN_LETTERS_NUMBER_EVERY_CASE_IN_PASSWORD
                + " letter(s) in upper and lower cases, "
            + ApiConstants.REQUIRED_MIN_CHARACTERS_NUMBER_IN_PASSWORD + " character(s), "
            + ApiConstants.REQUIRED_MIN_DIGITS_NUMBER_IN_PASSWORD + " digit(s). "),
    HAVE_NO_ACCESS("You don`t have the necessary permission"),
    ;

    private final String value;
    private final String formatTemplate;

    ApiErrorMessage(String value) {
        this.value = value;
        this.formatTemplate = value.replace("{}", "'%s'");
    }

    public String format(Object... args) {
        return String.format(formatTemplate, args);
    }
}
