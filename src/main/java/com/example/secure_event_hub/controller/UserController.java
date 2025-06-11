package com.example.secure_event_hub.controller;

import com.example.secure_event_hub.model.User;
import com.example.secure_event_hub.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "api/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("v1/register/signup")
    @ResponseStatus(CREATED)
    public ResponseEntity<Object> signUp(@RequestBody User user) {
        return ResponseHandler.success(userService.registerUser(user), "User registered Successfully", HttpStatus.CREATED);
    }

}
