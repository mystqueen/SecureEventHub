package com.example.secure_event_hub.exception;

public class RegistrationFailedException extends RuntimeException {
    public RegistrationFailedException(String message) {
        super(message);
    }
}
