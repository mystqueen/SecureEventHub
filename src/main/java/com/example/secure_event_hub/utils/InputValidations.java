package com.example.secure_event_hub.utils;

import java.util.regex.Pattern;

public class InputValidations {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PASSWORD_SPECIAL_CHAR_PATTERN = Pattern.compile(".*[!@#$%^&*].*");
    private static final Pattern PASSWORD_NUMBER_PATTERN = Pattern.compile(".*\\d.*");

    public static void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
        if (!PASSWORD_SPECIAL_CHAR_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException("Password must contain at least one special character (!@#$%^&*)");
        }
        if (!PASSWORD_NUMBER_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException("Password must contain at least one number");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}
