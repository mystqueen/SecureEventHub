package com.example.secure_event_hub.exception;

public  class AccountInactiveException extends RuntimeException{
    public AccountInactiveException(String message) {
        super(message);
    }
}