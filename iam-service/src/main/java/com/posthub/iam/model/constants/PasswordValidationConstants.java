package com.posthub.iam.model.constants;

public final class PasswordValidationConstants {

    private PasswordValidationConstants() {}

    public static final String PASSWORD_LETTERS_UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String PASSWORD_LETTERS_LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    public static final String PASSWORD_DIGITS = "0123456789";
    public static final String PASSWORD_CHARACTERS = "~`!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?";
    public static final Integer REQUIRED_MIN_PASSWORD_LENGTH = 8;
    public static final Integer REQUIRED_MIN_LETTERS_NUMBER_EVERY_CASE_IN_PASSWORD = 1;
    public static final Integer REQUIRED_MIN_DIGITS_NUMBER_IN_PASSWORD = 1;
    public static final Integer REQUIRED_MIN_CHARACTERS_NUMBER_IN_PASSWORD = 1;

    public static final String PASSWORD_ALL_CHARACTERS =
            PASSWORD_LETTERS_UPPER_CASE +
                    PASSWORD_LETTERS_LOWER_CASE +
                    PASSWORD_DIGITS +
                    PASSWORD_CHARACTERS;

}
