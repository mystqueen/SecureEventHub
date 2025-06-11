package com.example.secure_event_hub.exception;


import com.example.secure_event_hub.controller.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Object> handleUserAlreadyExistException(UserAlreadyExistException exception) {
        return  ResponseHandler.error(null, exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RegistrationFailedException.class)
    public ResponseEntity<Object> handleRegistrationFailedException(RegistrationFailedException exception) {
        return  ResponseHandler.error(null, exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Object> handleInvalidInputException(InvalidInputException exception) {
        return  ResponseHandler.error(null, exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception) {
        return  ResponseHandler.error(null, exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException exception){
        return   ResponseHandler.error(null, exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(AccountInactiveException.class)
    public ResponseEntity<Object> handleAccountInactiveException(AccountInactiveException exception){
        return   ResponseHandler.error(null, exception.getMessage(), HttpStatus.FORBIDDEN);
    }
}
