package com.posthub.iam.security.password;

import com.posthub.iam.model.constants.PasswordValidationConstants;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class PasswordValidator {

    public static boolean isNotValidPassword(String password) {
        if (password == null || password.isEmpty() || password.trim().isEmpty()) {
            return true;
        }
        String trim = password.trim();
        if (trim.length() < PasswordValidationConstants.REQUIRED_MIN_PASSWORD_LENGTH) {
            return true;
        }
        int charactersNumber = PasswordValidationConstants.REQUIRED_MIN_CHARACTERS_NUMBER_IN_PASSWORD;
        int lettersUCaseNumber = PasswordValidationConstants.REQUIRED_MIN_LETTERS_NUMBER_EVERY_CASE_IN_PASSWORD;
        int lettersLCaseNumber = PasswordValidationConstants.REQUIRED_MIN_LETTERS_NUMBER_EVERY_CASE_IN_PASSWORD;
        int digitsNumber = PasswordValidationConstants.REQUIRED_MIN_DIGITS_NUMBER_IN_PASSWORD;
        for (int i = 0; i < trim.length(); i++) {
            String currentLetter = String.valueOf(trim.charAt(i));
            if (!PasswordValidationConstants.PASSWORD_ALL_CHARACTERS.contains(currentLetter)) {
                return true;
            }
            charactersNumber -= PasswordValidationConstants.PASSWORD_CHARACTERS.contains(currentLetter) ? 1 : 0;
            lettersUCaseNumber -= PasswordValidationConstants.PASSWORD_LETTERS_UPPER_CASE.contains(currentLetter) ? 1 : 0;
            lettersLCaseNumber -= PasswordValidationConstants.PASSWORD_LETTERS_LOWER_CASE.contains(currentLetter) ? 1 : 0;
            digitsNumber -= PasswordValidationConstants.PASSWORD_DIGITS.contains(currentLetter) ? 1 : 0;
        }
        return ((charactersNumber > 0) || (lettersUCaseNumber > 0) || (lettersLCaseNumber > 0) || (digitsNumber > 0));
    }

}
