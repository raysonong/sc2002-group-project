package com.sc2002.utilities;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator {

    // Define the set of characters considered "special" for password validation.
    // You can customize this string based on your specific requirements.
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{};:'\",.<>/?|`~";

    // Minimum required length for a valid password.
    private static final int MIN_LENGTH = 8;

    /**
     * Validates a password against predefined security criteria. Criteria: 1.
     * Must be at least MIN_LENGTH (8) characters long. 2. Must contain at least
     * one special character (from SPECIAL_CHARACTERS). 3. Must contain at least
     * one uppercase letter. 4. Must contain at least one lowercase letter. 5.
     * Must contain at least one digit.
     *
     * @param password The password string to validate.
     * @return true if the password meets all criteria, false otherwise.
     */
    public static boolean isValid(String password) {
        if (password == null) {
            return false; // Null passwords are not valid
        }

        // Check length requirement first
        if (password.length() < MIN_LENGTH) {
            return false;
        }

        // Flags to track if each required character type is found
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        // Iterate through each character of the password
        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUpper = true;
            } else if (Character.isLowerCase(ch)) {
                hasLower = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (SPECIAL_CHARACTERS.indexOf(ch) >= 0) {
                // Check if the character is in our defined set of special characters
                hasSpecial = true;
            }

            // Optimization: If all conditions are met, we can exit early
            if (hasUpper && hasLower && hasDigit && hasSpecial) {
                break;
            }
        }

        // Return true only if all criteria (length + character types) are met
        return password.length() >= MIN_LENGTH && hasUpper && hasLower && hasDigit && hasSpecial;
    }

    /**
     * Validates a password and returns a list of messages for unmet criteria.
     * This provides more detailed feedback than the simple isValid method.
     *
     * @param password The password string to validate.
     * @return A list of strings describing the unmet criteria. An empty list
     * means the password is valid.
     */
    public static List<String> getValidationMessages(String password) {
        List<String> messages = new ArrayList<>();

        if (password == null || password.isEmpty()) {
            messages.add("Password cannot be empty.");
            return messages; // Return immediately if null/empty
        }

        // Check length
        if (password.length() < MIN_LENGTH) {
            messages.add("Password must be at least " + MIN_LENGTH + " characters long.");
        }

        // Check character types
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUpper = true;
            } else if (Character.isLowerCase(ch)) {
                hasLower = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (SPECIAL_CHARACTERS.indexOf(ch) >= 0) {
                hasSpecial = true;
            }
        }

        // Add messages for missing character types
        if (!hasUpper) {
            messages.add("Password must contain at least one uppercase letter.");
        }
        if (!hasLower) {
            messages.add("Password must contain at least one lowercase letter.");
        }
        if (!hasDigit) {
            messages.add("Password must contain at least one number.");
        }
        if (!hasSpecial) {
            messages.add("Password must contain at least one special character (e.g., " + SPECIAL_CHARACTERS.substring(0, 5) + "...).");
        }

        return messages; // If list is empty, password is valid according to criteria
    }
}
