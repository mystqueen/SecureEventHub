package com.example.secure_event_hub.controller;

import com.example.secure_event_hub.model.User;
import com.example.secure_event_hub.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "api/")
public class UserController {
    private final UserService userService;
    private String email;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("v1/register/signup")
    @ResponseStatus(CREATED)
    public ResponseEntity<Object> signUp(@RequestBody User user) {
        return ResponseHandler.success(userService.registerUser(user), "User registered Successfully", HttpStatus.CREATED);
    }

    @PostMapping("v1/register/verify-email")
    public ResponseEntity<Object> verifyUser(@RequestBody Map<String, String> body) {
        email = getEmailFromBody(body);
        String otp = body.get("otp");
        boolean verified = userService.verifyUser(email, otp);
        if (verified) {
            return ResponseHandler.success(email, "User verified successfully", HttpStatus.OK);
        } else {
            return ResponseHandler.error(email, "User verification failed", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("v1/verify-otp")
    public ResponseEntity<Object> verifyOtp(@RequestBody Map<String, String> body) {
        email = getEmailFromBody(body);
        String otp = body.get("otp");
        try{
            userService.verifyOtp(email, otp);
            return ResponseHandler.success(email, "Otp Verified", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.error(email, "Otp Verification failed", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("v1/resend-otp")
    public ResponseEntity<Object> resendVerificationOtp(@RequestBody Map<String, String> body) {
        email = getEmailFromBody(body);
        try {
            String message = userService.resendOtp(email);
            return ResponseHandler.success(email, message, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.error(email, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("v1/password-reset-request")
    public ResponseEntity<Object> resetPasswordRequest(@RequestBody Map<String, String> body) {
        email = getEmailFromBody(body);
        try{
            String message = userService.requestPasswordReset(email);
            return ResponseHandler.success(email, message, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.error(email, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("v1/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody Map<String, String> body) {
        email = getEmailFromBody(body);
        String password = body.get("password");
        try{
            String message = userService.resetPassword(email, password);
            return ResponseHandler.success(email, message, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.error(email, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private String getEmailFromBody(Map<String, String> body) {
        return body.get("email");
    }
}
